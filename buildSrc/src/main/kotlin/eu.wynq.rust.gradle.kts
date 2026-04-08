import org.gradle.api.DefaultTask
import org.gradle.api.attributes.Usage
import org.gradle.api.file.DirectoryProperty
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
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.api.file.ProjectLayout
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class RustExtension @Inject constructor(objects: ObjectFactory) {
    val toolchain: Property<String> = objects.property(String::class.java).convention("stable")
    val target: Property<String> = objects.property(String::class.java)
    val cargoArgs: ListProperty<String> = objects.listProperty(String::class.java).convention(emptyList())
    val crateName: Property<String> = objects.property(String::class.java)
    val isLibrary: Property<Boolean> = objects.property(Boolean::class.java).convention(false)

    val actualBinaryName: Property<String> = objects.property(String::class.java).convention(
        crateName.zip(isLibrary) { name, isLib ->
            val os = OperatingSystem.current()
            when {
                isLib -> os.getSharedLibraryName(name)
                else -> os.getExecutableName(name)
            }
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

        execOperations.exec {
            workingDir = cargoManifest.get().asFile.parentFile
            val cargoCommand = mutableListOf(
                "cargo",
                "+${toolchain.get()}",
                "build",
                "--release",
                "--manifest-path",
                cargoManifest.get().asFile.absolutePath,
                "--target-dir",
                cargoTargetDir.absolutePath,
            )
            target.orNull?.let {
                cargoCommand.addAll(listOf("--target", it))
            }
            cargoCommand.addAll(cargoArgs.get())
            commandLine(cargoCommand)
        }

        val builtBinary = File(targetOutputDir, outputFile.name)
        outputFile.parentFile.mkdirs()
        builtBinary.copyTo(outputFile, overwrite = true)
    }
}

private fun readLibName(cargoToml: File): String {
    var inLibSection = false
    for (line in cargoToml.readLines()) {
        val trimmed = line.trim()
        when {
            trimmed == "[lib]" -> inLibSection = true
            trimmed.startsWith("[") -> inLibSection = false
            inLibSection && trimmed.startsWith("name") -> return trimmed.substringAfter('=').trim().trim('"')
        }
    }
    error("Unable to determine [lib] name from ${cargoToml.absolutePath}")
}

fun operatingSystemFamilyName(): String = OperatingSystem.current().familyName

val rustExtension = extensions.create("rust", RustExtension::class.java)

val rustupSetup = tasks.register("rustupSetup", RustupSetupTask::class.java)

val cargoBuild = tasks.register("cargoBuild", CargoBuildTask::class.java) {
    dependsOn(rustupSetup)
    sourceDir.set(layout.projectDirectory.dir("src/main/rust/src"))
    cargoManifest.set(layout.projectDirectory.file("Cargo.toml"))
    toolchain.set(rustExtension.toolchain)
    cargoArgs.set(rustExtension.cargoArgs)
    target.set(rustExtension.target)
    crateName.set(rustExtension.crateName)
    library.set(rustExtension.isLibrary)
    outputBinary.set(layout.buildDirectory.file(rustExtension.actualBinaryName.map { "native/$it" }))
}

configurations.create("rustElements") {
    isCanBeConsumed = true
    isCanBeResolved = false
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.NATIVE_RUNTIME))
        attribute(
            OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE,
            objects.named(OperatingSystemFamily::class.java, operatingSystemFamilyName())
        )
    }
    outgoing.artifact(cargoBuild.flatMap { it.outputBinary }) {
        builtBy(cargoBuild)
    }
}

tasks.named("assemble") {
    dependsOn(cargoBuild)
}

val cargoToml = layout.projectDirectory.file("Cargo.toml").asFile
rustExtension.crateName.set(readLibName(cargoToml))
rustExtension.isLibrary.set(true)



