package eu.wynq.arkascendedservermanager.core.db.repositories

import eu.wynq.arkascendedservermanager.core.db.models.Settings
import eu.wynq.arkascendedservermanager.core.db.models.SettingsEntity
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object SettingsRepository {
    fun createDefaultSettings(): Result<Unit> = runCatching {
        transaction {
            if (SettingsEntity.all().empty()) {
                SettingsEntity.new { }
            }
        }
    }

    fun getSettings(): Result<Settings> = runCatching {
        transaction {
            val entity = SettingsEntity.all().firstOrNull()
                ?: throw IllegalStateException("Settings not found")

            Settings.fromEntity(entity)
        }
    }

    fun saveSettings(settings: Settings) = runCatching {
        transaction {
            val existingSettings = SettingsEntity.all().firstOrNull()
                ?: throw IllegalStateException("Settings not found")
            SettingsEntity.findByIdAndUpdate(existingSettings.id.value) {
                it.data_path = settings.dataPath
            }
        }
    }

    fun getDataPath(): Result<String> = runCatching {
        transaction {
            val entity = SettingsEntity.all().firstOrNull()
                ?: throw IllegalStateException("Settings not found")
            entity.data_path
        }
    }
}