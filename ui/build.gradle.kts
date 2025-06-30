import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinxSerialization)
}

val appVersion = when {
    project.hasProperty("version") && project.property("version") != "unspecified" -> {
        project.property("version").toString()
    }
    System.getProperty("version") != null -> {
        System.getProperty("version")
    }
    else -> "1.0.0"
}

// Task to generate version properties file
val generateVersionProperties by tasks.registering {
    val outputDir = file("${layout.buildDirectory.get()}/generated/resources")
    outputs.dir(outputDir)

    doLast {
        outputDir.mkdirs()
        val propsFile = File(outputDir, "version.properties")
        propsFile.writeText("version=$appVersion\n")
    }
}

// Make sure the task runs before processing resources
tasks.named("processResources") {
    dependsOn(generateVersionProperties)
}

// Add the generated resources to the source set
sourceSets {
    main {
        resources {
            srcDir(generateVersionProperties.map { it.outputs.files.singleFile })
        }
    }
}

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