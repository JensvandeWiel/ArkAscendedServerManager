import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinxSerialization)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(project(":core"))
    implementation(libs.decompose)
    implementation(libs.decomposeExtensions)
    implementation(libs.fluentUi)
    implementation(libs.fluentIconsExtended)
    implementation(libs.sonner)

    // Logging dependencies
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
    implementation(libs.logbackClassic)

    implementation(libs.nomenEstOmen)
    implementation(libs.steamCmd)
    implementation(libs.essenty)
}

compose.desktop {
    application {
        mainClass = "ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ArkAscendedServerManager"
            packageVersion = "1.0.0"
        }
    }
}
