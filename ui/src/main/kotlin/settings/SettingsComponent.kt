package ui.settings

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.dokar.sonner.ToastType
import io.github.oshai.kotlinlogging.KotlinLogging
import settings.SettingsHelper
import ui.ToastManager

class SettingsComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val _model: MutableValue<SettingsModel>
    private val logger = KotlinLogging.logger {}

    init {
        val settings = SettingsHelper().getSettings()
        if (settings.isFailure) {
            ToastManager.get()
                .toast(settings.exceptionOrNull()?.message ?: "Failed to load settings", type = ToastType.Error)
        }
        _model = MutableValue(
            settings.getOrNull()?.toSettingsModel() ?: SettingsModel(
                steamCmdPath = TextFieldValue(""),
                applicationDataPath = TextFieldValue("")
            )
        )
    }

    val model: Value<SettingsModel> get() = _model

    fun updateSteamCmdPath(newValue: TextFieldValue) {
        _model.value = _model.value.copy(steamCmdPath = newValue)
    }

    fun updateApplicationDataPath(newValue: TextFieldValue) {
        _model.value = _model.value.copy(applicationDataPath = newValue)
    }

    fun saveSettings() {
        val validationResult = _model.value.validate()
        if (!validationResult.isValid) {
            ToastManager.get().toast(validationResult.error ?: "Unknown error", type = ToastType.Error)
            logger.error { "Settings validation failed: ${validationResult.error}" }
            return
        }
        val settings = _model.value.toSettings()
        val result = SettingsHelper().saveSettings(settings)
        if (result.isFailure) {
            ToastManager.get()
                .toast(result.exceptionOrNull()?.message ?: "Failed to save settings", type = ToastType.Error)
            logger.error { "Failed to save settings: ${result.exceptionOrNull()?.message}" }
            return
        }
        ToastManager.get().toast("Settings saved successfully", type = ToastType.Success)
        logger.info { "Settings saved successfully: $settings" }
    }
}
