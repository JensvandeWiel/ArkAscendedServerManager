package ui.server

import Downloading
import Error
import Failed
import Installed
import Preparing
import Status
import SteamCMDInstalling
import SteamCMDUpdating
import Validating
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.dokar.sonner.ToastType
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private val scope = coroutineScope()
    var profileConfigurationModel: MutableValue<ProfileConfigurationModel> = MutableValue(
        ProfileConfigurationModel.fromServerProfile(server.value)
    )

    val installationModel: MutableValue<InstallationModel> = MutableValue(
        InstallationModel(isInstalling = false, status = Preparing())
    )

    fun onInstallationStatusUpdate(status: Status) {
        scope.launch(Dispatchers.Main) {
            when (status) {
                is SteamCMDUpdating -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = true,
                        status = status,
                        message = "Updating steamcmd",
                        progress = status.progress
                    )
                }

                is SteamCMDInstalling -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = true,
                        status = status,
                        message = "Installing steamcmd",
                        progress = null
                    )
                }

                is Downloading -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = true,
                        status = status,
                        message = "Downloading server",
                        progress = status.progress
                    )
                }

                is Error -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = false,
                        status = status,
                        message = "Error: ${status.exitCode}",
                        progress = null
                    )
                    ToastManager.get()
                        .toast("SteamCMD exited with code: ${status.exitCode}", type = ToastType.Error)
                    logger.error { "SteamCMD exited with code: ${status.exitCode}" }
                }

                is Failed -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = false,
                        status = status,
                        message = "Failed to install server: ${status.error.message}",
                        progress = null
                    )
                    ToastManager.get()
                        .toast("Failed to install server: ${status.error}", type = ToastType.Error)
                    logger.error { "Failed to install server: ${status.error}" }
                }

                is Installed -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = false,
                        status = status,
                        message = "Server installed successfully",
                        progress = null
                    )
                    ToastManager.get().toast("Server installed successfully", type = ToastType.Success)
                }

                is Preparing -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = true,
                        status = status,
                        message = "Preparing installation",
                        progress = null
                    )
                }

                is Validating -> {
                    installationModel.value = installationModel.value.copy(
                        isInstalling = true,
                        status = status,
                        message = "Validating installation",
                        progress = status.progress
                    )
                }
            }
        }
    }

    fun startServerInstallation() {
        scope.launch(Dispatchers.IO) {
            val serverManager = server.value.getServerManager()
            val installManager = serverManager.getInstallManager()

            withContext(Dispatchers.Main) {
                installationModel.value = InstallationModel(
                    isInstalling = true,
                    status = Preparing(),
                    message = "Starting " + if (installManager.isInstalled()) "update" else "installation",
                    progress = null
                )
            }

            val result = installManager.install { status ->
                onInstallationStatusUpdate(status)
            }

            withContext(Dispatchers.Main) {
                if (result.isFailure) {
                    ToastManager.get().toast(
                        "Installation failed: ${result.exceptionOrNull()?.message}",
                        type = ToastType.Error
                    )
                    installationModel.value = InstallationModel(
                        isInstalling = false,
                        message = "Installation failed: ${result.exceptionOrNull()?.message}",
                        progress = null,
                        status = Error(-1)
                    )
                    logger.error { "Installation failed: ${result.exceptionOrNull()?.message}" }
                } else {
                    logger.info { "Installation completed successfully" }
                }
            }
        }
    }

    fun updateProfileConfigurationModel(profileConfigurationModel: ProfileConfigurationModel) {
        this.profileConfigurationModel.value = profileConfigurationModel
    }

    fun saveProfileConfiguration() {
        scope.launch(Dispatchers.IO) {
            val validationResult = profileConfigurationModel.value.validate()
            if (!validationResult.isValid) {
                withContext(Dispatchers.Main) {
                    ToastManager.get().toast(validationResult.error ?: "Unknown error", type = ToastType.Error)
                    logger.error { "Validation failed: ${validationResult.error}" }
                }
                return@launch
            }

            val updatedServer = server.value.copy(
                profileName = profileConfigurationModel.value.profileName.text,
                installationLocation = profileConfigurationModel.value.installationLocation.text
            )

            val result = onUpdateServer(updatedServer)
            withContext(Dispatchers.Main) {
                if (result.isFailure) {
                    ToastManager.get().toast(
                        result.exceptionOrNull()?.message ?: "Failed to update server",
                        type = ToastType.Error
                    )
                    logger.error { "Failed to update server: ${result.exceptionOrNull()?.message}" }
                } else {
                    ToastManager.get().toast("Server updated successfully", type = ToastType.Success)
                }
            }
        }
    }
}
