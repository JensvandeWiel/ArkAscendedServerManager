import org.gradle.kotlin.dsl.maven

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val githubUsernameEnvKey = "GITHUB_USERNAME"
val githubTokenEnvKey = "GITHUB_TOKEN"
val githubUsernameGradlePropertyKey = "gpr.user"
val githubTokenGradlePropertyKey = "gpr.key"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

val envFile = file(".env")
val envProperties = mutableMapOf<String, String>()
if (envFile.exists()) {
    val properties = java.util.Properties()
    envFile.inputStream().use(properties::load)
    properties.forEach { key, value ->
        envProperties[key.toString()] = value.toString()
    }
}

// Source precedence: environment -> gradle.properties -> optional .env fallback.
fun resolveCredential(envKey: String, gradlePropertyKey: String): String? {
    return providers.environmentVariable(envKey).orNull
        ?: providers.gradleProperty(gradlePropertyKey).orNull
        ?: envProperties[envKey]
        ?: envProperties[gradlePropertyKey]
}

val githubUsername = resolveCredential(githubUsernameEnvKey, githubUsernameGradlePropertyKey)
val githubToken = resolveCredential(githubTokenEnvKey, githubTokenGradlePropertyKey)

if ((githubUsername.isNullOrBlank()) != (githubToken.isNullOrBlank())) {
    throw GradleException("GitHubPackages credentials are incomplete. Set both $githubUsernameEnvKey and $githubTokenEnvKey (or $githubUsernameGradlePropertyKey and $githubTokenGradlePropertyKey).")
}

dependencyResolutionManagement {
    repositories {
        maven("https://packages.jetbrains.team/maven/p/kpm/public/")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/JensvandeWiel/kotlin-steamcmd")
            if (!githubUsername.isNullOrBlank() && !githubToken.isNullOrBlank()) {
                credentials {
                    username = githubUsername
                    password = githubToken
                }
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "ArkAscendedServerManager"
include("ui")
include("core")