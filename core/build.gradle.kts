plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinxSerialization)
}

dependencies {
    implementation(libs.kotlinxSerializationJson)
    implementation(libs.nomenEstOmen)
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesSwing)
    implementation(libs.steamCmd)
    implementation(libs.ktor)
    implementation(libs.ktorCio)
    implementation(libs.oshi)

}
