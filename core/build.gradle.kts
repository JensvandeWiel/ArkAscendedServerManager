plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(libs.kotlinLogging)
    implementation(libs.slf4jApi)
    implementation(libs.logbackClassic)
    implementation(libs.logbackCore)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.h2)
    implementation(libs.flyway.core)
}
