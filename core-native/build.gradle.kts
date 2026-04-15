plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.rust)
}

dependencies {
    implementation(libs.jna.jpms)
    testImplementation(libs.kotlin.test)
}

rust {
    crateName.set("core_native")
    toolchain.set("1.94.1-x86_64-pc-windows-msvc")
    isLibrary.set(true)
    cargoProjectVersion.set(rootProject.version.toString())
}

