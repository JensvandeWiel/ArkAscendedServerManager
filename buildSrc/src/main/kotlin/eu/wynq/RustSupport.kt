package eu.wynq

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class RustExtension @Inject constructor(objects: ObjectFactory) {
    val toolchain: Property<String> = objects.property(String::class.java).convention("stable")
    val target: Property<String> = objects.property(String::class.java)
    val cargoArgs: ListProperty<String> = objects.listProperty(String::class.java).convention(emptyList())
    val cargoProjectVersion: Property<String> = objects.property(String::class.java)
    val crateName: Property<String> = objects.property(String::class.java)
    val isLibrary: Property<Boolean> = objects.property(Boolean::class.java).convention(false)

    val actualBinaryName: Property<String> = objects.property(String::class.java).convention(
        crateName.zip(isLibrary) { name, isLib ->
            val os = OperatingSystem.current()
            if (isLib) os.getSharedLibraryName(name) else os.getExecutableName(name)
        }
    )
}

abstract class RustupSetupTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {
    init {
        group = "rust"
        description = "Ensures the Rust toolchain is available via rustup."
    }

    @TaskAction
    fun verifyRustToolchain() {
        val result = execOperations.exec {
            commandLine("rustc", "--version")
            isIgnoreExitValue = true
        }
        if (result.exitValue != 0) {
            logger.lifecycle("Rust toolchain missing. Please install rustup.")
        }
    }
}

@CacheableTask
abstract class CargoBuildTask @Inject constructor(
    private val projectLayout: ProjectLayout,
    private val execOperations: ExecOperations
) : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceDir: DirectoryProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val cargoManifest: RegularFileProperty

    @get:Input
    abstract val toolchain: Property<String>

    @get:Input
    abstract val cargoArgs: ListProperty<String>

    @get:Optional
    @get:Input
    abstract val cargoProjectVersion: Property<String>

    @get:Optional
    @get:Input
    abstract val target: Property<String>

    @get:Input
    abstract val crateName: Property<String>

    @get:Input
    abstract val library: Property<Boolean>

    @get:OutputFile
    abstract val outputBinary: RegularFileProperty

    @TaskAction
    fun build() {
        val cargoTargetDir = projectLayout.buildDirectory.dir("cargo-target").get().asFile
        val targetOutputDir = target.orNull?.let { File(cargoTargetDir, "$it/release") } ?: File(cargoTargetDir, "release")
        val outputFile = outputBinary.get().asFile
        val originalManifest = cargoManifest.get().asFile
        val originalManifestText = originalManifest.readText()
        val overriddenManifestText = cargoProjectVersion.orNull?.let { version ->
            createManifestWithOverriddenPackageVersion(originalManifestText, version, originalManifest.absolutePath)
        }

        try {
            if (overriddenManifestText != null && overriddenManifestText != originalManifestText) {
                originalManifest.writeText(overriddenManifestText)
            }
            execOperations.exec {
                workingDir = originalManifest.parentFile
                val cargoCommand = mutableListOf(
                    "cargo",
                    "+${toolchain.get()}",
                    "build",
                    "--release",
                    "--manifest-path",
                    originalManifest.absolutePath,
                    "--target-dir",
                    cargoTargetDir.absolutePath,
                )
                target.orNull?.let { cargoCommand.addAll(listOf("--target", it)) }
                cargoCommand.addAll(cargoArgs.get())
                commandLine(cargoCommand)
            }
        } finally {
            if (overriddenManifestText != null) {
                originalManifest.writeText(originalManifestText)
            }
        }

        val builtBinary = File(targetOutputDir, outputFile.name)
        outputFile.parentFile.mkdirs()
        builtBinary.copyTo(outputFile, overwrite = true)
    }
}

fun hasCargoSection(cargoToml: File, section: String): Boolean {
    val sectionHeader = "[$section]"
    return cargoToml.readLines().any { it.trim() == sectionHeader }
}

private fun readSectionName(cargoToml: File, section: String): String? {
    var inSection = false
    val sectionHeader = "[$section]"
    for (line in cargoToml.readLines()) {
        val trimmed = line.trim()
        when {
            trimmed == sectionHeader -> inSection = true
            trimmed.startsWith("[") -> inSection = false
            inSection && trimmed.startsWith("name") -> return trimmed.substringAfter('=').trim().trim('"')
        }
    }
    return null
}

fun inferCrateName(cargoToml: File, isLibrary: Boolean): String {
    val packageName = readSectionName(cargoToml, "package")
    if (isLibrary) {
        return readSectionName(cargoToml, "lib")
            ?: packageName?.replace('-', '_')
            ?: error("Unable to determine crate name from ${cargoToml.absolutePath}")
    }

    return packageName
        ?: readSectionName(cargoToml, "lib")
        ?: error("Unable to determine crate name from ${cargoToml.absolutePath}")
}

private fun createManifestWithOverriddenPackageVersion(cargoTomlContent: String, version: String, manifestPath: String): String {
    val packageHeader = "[package]"
    var inPackage = false
    var updatedVersion = false
    val updatedLines = cargoTomlContent.lines().map { line ->
        val trimmed = line.trim()
        when {
            trimmed == packageHeader -> {
                inPackage = true
                line
            }

            trimmed.startsWith("[") -> {
                inPackage = false
                line
            }

            inPackage && trimmed.substringBefore("=").trim() == "version" -> {
                updatedVersion = true
                val indent = line.takeWhile { it == ' ' || it == '\t' }
                "${indent}version = \"$version\""
            }

            else -> line
        }
    }

    require(updatedVersion) {
        "Unable to override Cargo package version; no version key found in [package] section of $manifestPath"
    }

    return updatedLines.joinToString(System.lineSeparator())
}

