import org.gradle.api.attributes.Usage
import org.gradle.api.tasks.Copy
import org.gradle.internal.os.OperatingSystem
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.nativeplatform.OperatingSystemFamily
import java.security.MessageDigest

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.serialization)
}

fun operatingSystemFamilyName(): String = OperatingSystem.current().familyName

val overseerRuntime = configurations.create("overseerRuntime") {
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

val generatedOverseerResourceDir = layout.buildDirectory.dir("generated/resources/overseer")
val syncOverseerResource = tasks.register<Copy>("syncOverseerResource") {
    from(overseerRuntime)
    into(generatedOverseerResourceDir)
    rename("overseer.exe", "Overseer.exe")
}

val writeOverseerChecksum = tasks.register("writeOverseerChecksum") {
    dependsOn(syncOverseerResource)
    inputs.dir(generatedOverseerResourceDir)
    outputs.file(generatedOverseerResourceDir.map { it.file("Overseer.exe.sha256") })

    doLast {
        val resourceDir = generatedOverseerResourceDir.get().asFile
        val executable = resourceDir.resolve("Overseer.exe")
        check(executable.exists()) { "Expected overseer executable at ${executable.absolutePath}" }

        val digest = MessageDigest.getInstance("SHA-256")
        executable.inputStream().use { input ->
            val buffer = ByteArray(8192)
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                digest.update(buffer, 0, read)
            }
        }
        val checksum = digest.digest().joinToString("") { "%02x".format(it) }
        resourceDir.resolve("Overseer.exe.sha256").writeText(checksum)
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(writeOverseerChecksum)
    from(generatedOverseerResourceDir) {
        into("overseer")
    }
}

dependencies {
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
    implementation(libs.logbackClassic)
    implementation(libs.logbackCore)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.nomenEstOmen)
    implementation(libs.h2)
    implementation(libs.flyway.core)
    implementation(libs.steamCmd)
    implementation(project(":core-native"))
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinxJson)
    implementation(libs.semver)
    implementation(libs.exposed.json)
    implementation(libs.serialization.json)
    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.oshi)
    testImplementation(libs.kotlin.test)
    implementation(libs.inisaur)
    add("overseerRuntime", project(":overseer"))
}
