import eu.wynq.CargoBuildTask
import eu.wynq.RustExtension
import eu.wynq.RustupSetupTask
import eu.wynq.hasCargoSection
import eu.wynq.inferCrateName
import org.gradle.api.attributes.Usage
import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.OperatingSystemFamily

fun operatingSystemFamilyName(): String = OperatingSystem.current().familyName

val rustExtension = extensions.create("rust", RustExtension::class.java)

val rustupSetup = tasks.register("rustupSetup", RustupSetupTask::class.java)

val cargoBuild = tasks.register("cargoBuild", CargoBuildTask::class.java) {
    dependsOn(rustupSetup)
    sourceDir.set(layout.projectDirectory.dir("src/main/rust/src"))
    cargoManifest.set(layout.projectDirectory.file("Cargo.toml"))
    toolchain.set(rustExtension.toolchain)
    cargoArgs.set(rustExtension.cargoArgs)
    cargoProjectVersion.set(rustExtension.cargoProjectVersion)
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
val inferredLibrary = hasCargoSection(cargoToml, "lib")
rustExtension.isLibrary.convention(inferredLibrary)
rustExtension.crateName.convention(inferCrateName(cargoToml, inferredLibrary))
val effectiveVersion = if (version.toString() != "unspecified") version.toString() else rootProject.version.toString()
if (effectiveVersion != "unspecified") {
    rustExtension.cargoProjectVersion.convention(effectiveVersion)
}
