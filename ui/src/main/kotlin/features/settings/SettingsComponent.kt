package eu.wynq.arkascendedservermanager.ui.features.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import eu.wynq.arkascendedservermanager.ui.stores.SettingsStore
import eu.wynq.arkascendedservermanager.core.db.models.Settings
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsComponent(componentContext: ComponentContext) : ComponentContext by componentContext, KoinComponent {
    val settingsStore: SettingsStore by inject()
    private val _model: MutableValue<SettingsModel>
    val model: Value<SettingsModel> get() = _model


    init {
        val initialSettings = settingsStore.settings.value
        _model = MutableValue(
            SettingsModel(
                dataPath = initialSettings?.dataPath ?: "",
                initialDataPath = initialSettings?.dataPath ?: "",
                steamCmdPath = initialSettings?.steamcmdPath ?: "",
                initialSteamCmdPath = initialSettings?.steamcmdPath ?: ""
            )
        )
    }

    fun onDataPathChanged(value: String) {
        val updatedModel = _model.value.copy(dataPath = value)
        _model.value = updatedModel.copy(dataPathError = !updatedModel.isDataPathValid())
    }

    fun onSteamCmdPathChanged(value: String) {
        val updatedModel = _model.value.copy(steamCmdPath = value)
        _model.value = updatedModel.copy(steamCmdPathError = !updatedModel.isSteamCmdPathValid())
    }

    fun saveSettings() {
        val currentModel = _model.value
        if (!currentModel.isDataPathValid()) {
            _model.value = currentModel.copy(dataPathError = true)
            return
        }

        if (!currentModel.isSteamCmdPathValid()) {
            _model.value = currentModel.copy(steamCmdPathError = true)
            return
        }

        if (!currentModel.isDirty()) return

        settingsStore.updateSettings(Settings(dataPath = currentModel.dataPath, steamcmdPath = currentModel.steamCmdPath))

        val hasSaveError = settingsStore.error.value != null
        _model.value = if (hasSaveError) {
            ToastBannerManager.show(ToastBannerType.ERROR, "Failed to save settings")
            currentModel.copy(dataPathError = true, steamCmdPathError = true)
        } else {
            currentModel.copy(
                dataPathError = false,
                steamCmdPathError = false,
                initialDataPath = currentModel.dataPath,
                initialSteamCmdPath = currentModel.steamCmdPath
            )
        }
    }
}