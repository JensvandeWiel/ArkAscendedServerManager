plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(libs.jna.jpms)
    testImplementation(libs.kotlin.test)
}

val rustSourceDir = layout.projectDirectory.dir("src/main/rust")
val cargoManifest = layout.projectDirectory.file("Cargo.toml")
val cargoTargetDir = layout.buildDirectory.dir("cargo")
val nativeOutputDir = layout.buildDirectory.dir("native")
val nativeLibraryName = System.mapLibraryName("core_native")
val cargoProfile = providers.environmentVariable("CORE_NATIVE_CARGO_PROFILE")
    .orElse("release")
    .map { profile -> if (profile.equals("release", ignoreCase = true)) "release" else "debug" }
    .get()
val builtNativeLibrary = cargoTargetDir.map { it.file("$cargoProfile/$nativeLibraryName") }

val cargoBuild = tasks.register<Exec>("cargoBuild") {
    workingDir = layout.projectDirectory.asFile
    val cargoCommand = mutableListOf(
        "cargo",
        "build",
        "--manifest-path",
        cargoManifest.asFile.absolutePath,
        "--target-dir",
        cargoTargetDir.get().asFile.absolutePath,
    )
    if (cargoProfile == "release") {
        cargoCommand.add("--release")
    }
    commandLine(cargoCommand)

    inputs.file(cargoManifest)
    inputs.files(fileTree(rustSourceDir.asFile) { include("src/**/*.rs") })
    outputs.file(builtNativeLibrary)
}

val copyNativeLibrary = tasks.register<Copy>("copyNativeLibrary") {
    dependsOn(cargoBuild)
    from(builtNativeLibrary)
    into(nativeOutputDir)
    outputs.file(nativeOutputDir.map { it.file(nativeLibraryName) })
}

tasks.named<Jar>("jar") {
    dependsOn(copyNativeLibrary)
    from(builtNativeLibrary) {
        into("win32-x86-64")
        rename { "core_native.dll" }
    }
}

tasks.named("compileKotlin") {
    dependsOn(cargoBuild)
}

tasks.named("processResources") {
    dependsOn(copyNativeLibrary)
}

tasks.named("assemble") {
    dependsOn(copyNativeLibrary)
}

tasks.named<Test>("test") {
    dependsOn(copyNativeLibrary)
    systemProperty("core.native.library.path", nativeOutputDir.get().asFile.absolutePath)
}



