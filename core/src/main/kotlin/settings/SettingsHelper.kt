package settings

import kotlinx.serialization.json.Json
import java.io.File

class SettingsHelper {
    fun getSettings(): Result<Settings> {
        val path = System.getProperty("user.home") + "/.aasm/settings.json"
        try {
            val file = File(path)
            if (file.exists()) {
                val json = file.readText()
                val settings = Json.decodeFromString<Settings>(json)
                return Result.success(settings)
            } else {
                val settings = Settings()
                saveSettings(settings)
                return Result.success(settings)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun saveSettings(settings: Settings): Result<Unit> {
        val path = System.getProperty("user.home") + "/.aasm/settings.json"
        try {
            val file = File(path)
            file.parentFile.mkdirs()
            val json = Json { prettyPrint = true; encodeDefaults = true }
            file.writeText(json.encodeToString(settings))
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(Unit)
    }
}