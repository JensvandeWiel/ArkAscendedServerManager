group = "eu.wynq.arkascendedservermanager"

val configuredVersion = providers.gradleProperty("releaseVersion")
    .orElse(providers.environmentVariable("RELEASE_VERSION"))
    .orElse("0.0.0")
    .map { it.removePrefix("v") }

version = configuredVersion.get()

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.nucleus) apply false
    alias(libs.plugins.rust) apply false
}