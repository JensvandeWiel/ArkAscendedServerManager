package eu.wynq.arkascendedservermanager.ui.features.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import eu.wynq.arkascendedservermanager.ui.stores.SettingsStore
import eu.wynq.arkascendedservermanager.core.db.models.Settings
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
                initialDataPath = initialSettings?.dataPath ?: ""
            )
        )
    }

    fun onDataPathChanged(value: String) {
        val updatedModel = _model.value.copy(dataPath = value)
        _model.value = updatedModel.copy(dataPathError = !updatedModel.isDataPathValid())
    }

    fun saveSettings() {
        val currentModel = _model.value
        if (!currentModel.isDataPathValid()) {
            _model.value = currentModel.copy(dataPathError = true)
            return
        }

        if (!currentModel.isDirty()) return

        settingsStore.updateSettings(Settings(dataPath = currentModel.dataPath))

        val hasSaveError = settingsStore.error.value != null
        _model.value = if (hasSaveError) {
            currentModel.copy(dataPathError = false)
        } else {
            currentModel.copy(
                dataPathError = false,
                initialDataPath = currentModel.dataPath,
            )
        }
    }
}