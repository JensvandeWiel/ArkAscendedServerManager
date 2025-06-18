// Load environment variables from .env file
val envFile = file(".env")
if (envFile.exists()) {
    val properties = java.util.Properties()
    properties.load(envFile.inputStream())
    properties.forEach { key, value ->
        if (System.getenv(key.toString()) == null) {
            System.setProperty(key.toString(), value.toString())
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/JensvandeWiel/kotlin-steamcmd")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                    ?: project.findProperty("gpr.user") as String?
                            ?: System.getProperty("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
                    ?: project.findProperty("gpr.key") as String?
                            ?: System.getProperty("GITHUB_TOKEN")
            }
        }
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

group = "eu.wynq.arkascendedservermanager"

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

