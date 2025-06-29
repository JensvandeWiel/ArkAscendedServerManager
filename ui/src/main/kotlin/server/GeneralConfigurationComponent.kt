package ui.server

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

class GeneralConfigurationComponent (
    componentContext: ComponentContext,
    val generalConfigurationModel: MutableValue<GeneralConfigurationModel>,
    val gameUserSettingsModel: MutableValue<GameUserSettingsModel>
) : ComponentContext by componentContext {

    fun updateAdministrationModel(generalConfigurationModel: GeneralConfigurationModel) {
        this.generalConfigurationModel.value = generalConfigurationModel
    }

    fun updateGameUserSettingsModel(gameUserSettingsModel: GameUserSettingsModel) {
        this.gameUserSettingsModel.value = gameUserSettingsModel
    }

    fun updateModsList(updatedMods: MutableList<Int>) {
        generalConfigurationModel.value = generalConfigurationModel.value.copy(mods = updatedMods)
    }
}