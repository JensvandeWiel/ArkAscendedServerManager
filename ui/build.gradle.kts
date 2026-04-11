import org.gradle.api.attributes.Usage
import io.github.kdroidfilter.nucleus.desktop.application.dsl.TargetFormat
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.JavaExec
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.OperatingSystemFamily

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.nucleus)
    alias(libs.plugins.serialization)
}

fun operatingSystemFamilyName(): String = OperatingSystem.current().familyName

val nativeRuntimeConfig = configurations.create("nativeRuntime") {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.NATIVE_RUNTIME))
        attribute(
            OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE,
            objects.named(OperatingSystemFamily::class.java, operatingSystemFamilyName())
        )
    }
}

dependencies {
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.components.resources)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material")
    }

    implementation(libs.kotlinx.coroutinesSwing)
    implementation(libs.jewel.intUiStandalone)
    implementation(libs.jewel.intUiDecoratedWindow)
    implementation(libs.nucleus.jewelDecoratedWindow)
    implementation(libs.nucleus.darkModeDetector)
    implementation(libs.nucleus.decoratedWindowJbr)
    implementation(libs.nucleus.systemColor)
    implementation(libs.intellij.icons)
    implementation(libs.jna.jpms)
    implementation(libs.decompose.core)
    implementation(libs.decompose.extensions)
    implementation(libs.serialization.json)
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
    implementation(libs.logbackClassic)
    implementation(libs.logbackCore)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    testImplementation(libs.kotlin.test)
    implementation(project(":core"))
    add("nativeRuntime", project(":core-native"))
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.steamCmd)
    implementation(libs.inisaur)
}

val nativeLibsDir = layout.buildDirectory.dir("native-libs")
val appVersion = rootProject.version.toString()
val installerVersion = appVersion.substringBefore('+').substringBefore('-').ifBlank { "0.0.0" }
val generatedBuildInfoDir = layout.buildDirectory.dir("generated/resources/build-info")

val generateBuildInfo = tasks.register("generateBuildInfo") {
    val outputFile = generatedBuildInfoDir.map { it.file("app-build-info.properties") }
    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText("version=$appVersion\n")
    }
}

sourceSets {
    named("main") {
        resources.srcDir(generatedBuildInfoDir)
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(generateBuildInfo)
}

val syncNativeLibs = tasks.register<Sync>("syncNativeLibs") {
    from(nativeRuntimeConfig)
    into(nativeLibsDir)
}

tasks.withType<JavaExec>().configureEach {
    dependsOn(syncNativeLibs)
    systemProperty("jna.library.path", nativeLibsDir.get().asFile.absolutePath)
}

tasks.withType<Test>().configureEach {
    dependsOn(syncNativeLibs)
    systemProperty("jna.library.path", nativeLibsDir.get().asFile.absolutePath)
}


nucleus.application {
    mainClass = "eu.wynq.arkascendedservermanager.ui.MainKt"

    nativeDistributions {
        targetFormats(TargetFormat.Nsis)
        packageName = "ArkAscendedServerManager"
        packageVersion = installerVersion
        modules("java.sql")
    }
    buildTypes {
        release {
            proguard {
                isEnabled = false
            }
        }
    }
}





java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

