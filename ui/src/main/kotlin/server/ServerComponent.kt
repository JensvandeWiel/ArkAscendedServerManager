package ui.server

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.konyaco.fluent.component.Text
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import server.ServerProfile
import ui.ToastManager
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ServerComponent(
    componentContext: ComponentContext,
    val server: Value<ServerProfile>,
    val onUpdateServer: (ServerProfile) -> Result<Unit>
) : ComponentContext by componentContext {
    private var logger = KotlinLogging.logger { }
    var profileConfigurationModel: MutableValue<ProfileConfigurationModel> = MutableValue(
        ProfileConfigurationModel.fromServerProfile(server.value)
    )

    fun updateProfileConfigurationModel(profileConfigurationModel: ProfileConfigurationModel) {
        this.profileConfigurationModel.value = profileConfigurationModel
    }

    fun saveProfileConfiguration() {
        val validationResult = profileConfigurationModel.value.validate()
        if (!validationResult.isValid) {
            ToastManager.get().toast(validationResult.error ?: "Unknown error", type = com.dokar.sonner.ToastType.Error)
            logger.error { "Validation failed: ${validationResult.error}" }
        }

        val updatedServer = server.value.copy(
            profileName = profileConfigurationModel.value.profileName.text,
            installationLocation = profileConfigurationModel.value.installationLocation.text
        )

        val result = onUpdateServer(updatedServer)
        if (result.isFailure) {
            ToastManager.get().toast(
                result.exceptionOrNull()?.message ?: "Failed to update server",
                type = com.dokar.sonner.ToastType.Error
            )
            logger.error { "Failed to update server: ${result.exceptionOrNull()?.message}" }
        } else {
            ToastManager.get().toast("Server updated successfully", type = com.dokar.sonner.ToastType.Success)
        }
    }
}