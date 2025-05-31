plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinxSerialization)
}

dependencies {
    implementation(libs.kotlinxSerializationJson)
}
