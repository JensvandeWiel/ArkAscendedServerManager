package ui

import java.util.Properties

object VersionInfo {
    val version: String by lazy {
        try {
            val properties = Properties()
            val inputStream = VersionInfo::class.java.classLoader.getResourceAsStream("version.properties")
            if (inputStream != null) {
                properties.load(inputStream)
                properties.getProperty("version", "Unknown")
            } else {
                "Unknown"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}
