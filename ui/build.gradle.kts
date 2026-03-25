import io.github.kdroidfilter.nucleus.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.nucleus)
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
    implementation(libs.jna.jpms)

    testImplementation(libs.kotlin.test)
}


nucleus.application {
    mainClass = "ui.MainKt"

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

