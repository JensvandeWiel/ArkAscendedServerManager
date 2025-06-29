package ui.server

import androidx.compose.ui.text.input.TextFieldValue
import server.GameUserSettings
import server.MessageOfTheDay
import server.ServerSettings
import ui.ValidationResult

data class GameUserSettingsModel(
    val autoSavePeriod: TextFieldValue,
    val messageOfTheDay: TextFieldValue = TextFieldValue(""),
    val motdDuration: TextFieldValue = TextFieldValue("20")
) {
    fun toGameUserSettings(): GameUserSettings {
        return GameUserSettings(
            serverSettings = ServerSettings(
                autoSavePeriodMinutes = autoSavePeriod.text.toFloatOrNull() ?: 15f
            ),
            messageOfTheDay = MessageOfTheDay(
                duration = motdDuration.text.toIntOrNull() ?: 20,
                message = messageOfTheDay.text
            )
        )
    }

    companion object {
        fun fromGameUserSettings(gameUserSettings: GameUserSettings): GameUserSettingsModel {
            return GameUserSettingsModel(
                autoSavePeriod = TextFieldValue(gameUserSettings.serverSettings.autoSavePeriodMinutes.toString()),
                messageOfTheDay = TextFieldValue(gameUserSettings.messageOfTheDay!!.message ?: ""),
                motdDuration = TextFieldValue(gameUserSettings.messageOfTheDay!!.duration.toString())
            )
        }
    }

    fun validate(): ValidationResult<GameUserSettingsModel> {
        val autoSavePeriodValue = autoSavePeriod.text.toFloatOrNull()
        if (autoSavePeriodValue == null || autoSavePeriodValue <= 0) {
            return ValidationResult.invalid("Auto-save period must be a positive number")
        }

        val motdDurationValue = motdDuration.text.toIntOrNull()
        if (motdDurationValue == null || motdDurationValue <= 0) {
            return ValidationResult.invalid("Message of the Day duration must be a positive integer")
        }

        return ValidationResult.valid()
    }
}
