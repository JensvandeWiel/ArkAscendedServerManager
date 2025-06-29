package ui.server

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

class ProfileConfigurationComponent(
    componentContext: ComponentContext,
    val profileConfigurationModel: MutableValue<ProfileConfigurationModel>
) : ComponentContext by componentContext {

    fun updateProfileConfigurationModel(profileConfigurationModel: ProfileConfigurationModel) {
        this.profileConfigurationModel.value = profileConfigurationModel
    }
}
