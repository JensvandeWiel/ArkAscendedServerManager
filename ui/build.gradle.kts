import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

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
abstract class GenerateVersionPropertiesTask : DefaultTask() {
    @get:Input
    abstract val version: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generateProperties() {
        val outputDir = outputDirectory.get().asFile
        outputDir.mkdirs()
        val propsFile = File(outputDir, "version.properties")
        propsFile.writeText("version=${version.get()}\n")
    }
}

val generateVersionProperties = tasks.register<GenerateVersionPropertiesTask>("generateVersionProperties") {
    version.set(appVersion)
    outputDirectory.set(layout.buildDirectory.dir("generated/resources"))
}

// Make sure the task runs before processing resources
tasks.named("processResources") {
    dependsOn(generateVersionProperties)
}

// Add the generated resources to the source set
sourceSets {
    main {
        resources {
            srcDir(generateVersionProperties.map { it.outputDirectory.get() })
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