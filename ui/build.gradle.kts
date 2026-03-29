import io.github.kdroidfilter.nucleus.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.nucleus)
    alias(libs.plugins.serialization)
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
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
}


nucleus.application {
    mainClass = "eu.wynq.arkascendedservermanager.ui.MainKt"

    nativeDistributions {
        targetFormats(TargetFormat.Nsis)
        packageName = "ArkAscendedServerManager"
        packageVersion = "0.0.0"
    }
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

