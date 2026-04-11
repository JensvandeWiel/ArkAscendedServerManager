package eu.wynq.arkascendedservermanager.ui.helpers

import java.util.Properties

object AppBuildInfo {
    private const val BUILD_INFO_RESOURCE = "app-build-info.properties"

    val version: String by lazy {
        val properties = Properties()
        AppBuildInfo::class.java.classLoader
            .getResourceAsStream(BUILD_INFO_RESOURCE)
            ?.use(properties::load)

        properties.getProperty("version")?.takeIf { it.isNotBlank() } ?: "dev"
    }
}

