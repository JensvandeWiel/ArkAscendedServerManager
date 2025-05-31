package ui.settings

import androidx.compose.ui.text.input.TextFieldValue
import settings.Settings
import ui.ValidationResult

data class SettingsModel(
    val steamCmdPath: TextFieldValue,
    val applicationDataPath: TextFieldValue
) {
    fun validate(): ValidationResult<SettingsModel> {
        if (steamCmdPath.text.isBlank()) {
            return ValidationResult.invalid("SteamCMD path cannot be empty")
        }

        if (applicationDataPath.text.isBlank()) {
            return ValidationResult.invalid("Application data path cannot be empty")
        }

        return ValidationResult.valid()
    }
}

fun SettingsModel.toSettings(): Settings {
    return Settings(
        steamCmdPath = steamCmdPath.text,
        applicationDataPath = applicationDataPath.text
    )
}

fun Settings.toSettingsModel(): SettingsModel {
    return SettingsModel(
        steamCmdPath = TextFieldValue(steamCmdPath),
        applicationDataPath = TextFieldValue(applicationDataPath)
    )
}
