import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinxSerialization)
}

// Debug the version resolution
val versionProperty = project.findProperty("version") as String?
val projectVersion = project.version.toString()

// Use the passed version property, fallback to 1.0.0
val appVersion = versionProperty?.takeIf { it != "unspecified" } ?: "1.0.0"
project.version = appVersion

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
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
    implementation(libs.inisaur)
}

compose.desktop {
    application {
        mainClass = "ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "ArkAscendedServerManager"
            packageVersion = appVersion

            windows {
                menu = true
                shortcut = true
                includeAllModules = true
            }

            modules("java.base", "java.desktop", "java.logging")
        }

        buildTypes.release.proguard {
            configurationFiles.from("proguard-rules.pro")
        }
    }
}