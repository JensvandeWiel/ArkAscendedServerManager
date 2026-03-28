plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
    implementation(libs.logbackClassic)
    implementation(libs.logbackCore)
}
