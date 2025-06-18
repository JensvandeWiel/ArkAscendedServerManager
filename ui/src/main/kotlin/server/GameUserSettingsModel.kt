package ui.server

import androidx.compose.ui.text.input.TextFieldValue
import server.GameUserSettings
import server.ServerSettings
import ui.ValidationResult

data class GameUserSettingsModel(
    val autoSavePeriod: TextFieldValue,
) {
    fun toGameUserSettings(): GameUserSettings {
        return GameUserSettings(
            serverSettings = ServerSettings(
                autoSavePeriodMinutes = autoSavePeriod.text.toFloatOrNull() ?: 15f
            )
        )
    }

    companion object {
        fun fromGameUserSettings(gameUserSettings: GameUserSettings): GameUserSettingsModel {
            return GameUserSettingsModel(
                autoSavePeriod = TextFieldValue(gameUserSettings.serverSettings.autoSavePeriodMinutes.toString())
            )
        }
    }

    fun validate(): ValidationResult<GameUserSettingsModel> {
        val autoSavePeriodValue = autoSavePeriod.text.toFloatOrNull()
        if (autoSavePeriodValue == null || autoSavePeriodValue <= 0) {
            return ValidationResult.invalid("Auto-save period must be a positive number")
        }
        return ValidationResult.valid()
    }
}
