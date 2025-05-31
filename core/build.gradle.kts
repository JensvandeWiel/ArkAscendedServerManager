plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinxSerialization)
}

dependencies {
    implementation(libs.kotlinxSerializationJson)
    implementation(libs.nomenEstOmen)
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
}
