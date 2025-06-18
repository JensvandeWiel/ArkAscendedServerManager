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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import server.ProfileLoader
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

    private val _isRunning: MutableValue<Boolean> = MutableValue(
        (server.value.getServerManager().getPowerManager().isRunning().getOrNull() != null) ?: false
    )

    val isRunning: Value<Boolean> = _isRunning

    suspend fun updateServerRunningState() {
        return withContext(Dispatchers.IO) {
            try {
                val isRunningAtm = server.value.getServerManager().getPowerManager().isRunning().getOrNull() != null
                _isRunning.value = isRunningAtm
                logger.debug { "Polled server running state: ${server.value.uuid} is: " + isRunningAtm }
            } catch (e: Exception) {
                logger.error(e) { "Error checking server running state" }
                _isRunning.value = false
            }
        }
    }

    suspend fun startServer(): Result<Unit> {

        val res = ProfileLoader.updateStartupScript(server.value)
        if (res.isFailure) {
            logger.error(res.exceptionOrNull()) { "Failed to update startup script" }
            return Result.failure(res.exceptionOrNull() ?: Exception("Unknown error"))
        }

        return withContext(Dispatchers.IO) {
            try {
                val powerManager = server.value.getServerManager().getPowerManager()
                if (powerManager.isRunning().getOrNull() != null) {
                    return@withContext Result.failure(Exception("Server is already running."))
                }
                val result = powerManager.startServer()
                if (result.isFailure) {
                    logger.error(result.exceptionOrNull()) { "Failed to start server" }
                    return@withContext Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
                }
                withContext(Dispatchers.Main) {
                    updateServerRunningState()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                logger.error(e) { "Failed to start server" }
                Result.failure(e)
            }
        }
    }

    suspend fun stopServer(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val powerManager = server.value.getServerManager().getPowerManager()
                if (powerManager.isRunning().getOrNull() == null) {
                    return@withContext Result.failure(Exception("Server is not running."))
                }
                val result = powerManager.stopServer()
                if (result.isFailure) {
                    logger.error(result.exceptionOrNull()) { "Failed to stop server" }
                    return@withContext Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
                }
                withContext(Dispatchers.Main) {
                    updateServerRunningState()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                logger.error(e) { "Failed to stop server" }
                Result.failure(e)
            }
        }
    }


    init {
        // Poll the server running state every 5 seconds
        scope.launch(Dispatchers.IO) {
            while (true) {
                updateServerRunningState()
                kotlinx.coroutines.delay(5000)
            }
        }
    }

    val installationModel: MutableValue<InstallationModel> = MutableValue(
        InstallationModel(isInstalling = false, status = Preparing())
    )
    val administrationModel: MutableValue<AdministrationModel> =
        MutableValue(AdministrationModel.fromAdministrationConfig(server.value.administrationConfig))

    val gameUserSettingsModel: MutableValue<GameUserSettingsModel> =
        MutableValue(GameUserSettingsModel.fromGameUserSettings(server.value.gameUserSettings))

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

    fun updateAdministrationModel(administrationModel: AdministrationModel) {
        this.administrationModel.value = administrationModel
    }

    fun updateGameUserSettingsModel(gameUserSettingsModel: GameUserSettingsModel) {
        this.gameUserSettingsModel.value = gameUserSettingsModel
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

            val administrationValidationResult = administrationModel.value.validate()
            if (!administrationValidationResult.isValid) {
                withContext(Dispatchers.Main) {
                    ToastManager.get()
                        .toast(administrationValidationResult.error ?: "Unknown error", type = ToastType.Error)
                    logger.error { "Administration validation failed: ${administrationValidationResult.error}" }
                }
                return@launch
            }

            val gameUserSettingsValidationResult = gameUserSettingsModel.value.validate()
            if (!gameUserSettingsValidationResult.isValid) {
                withContext(Dispatchers.Main) {
                    ToastManager.get()
                        .toast(gameUserSettingsValidationResult.error ?: "Unknown error", type = ToastType.Error)
                    logger.error { "Game user settings validation failed: ${gameUserSettingsValidationResult.error}" }
                }
                return@launch
            }

            val updatedServer = server.value.copy(
                profileName = profileConfigurationModel.value.profileName.text,
                installationLocation = profileConfigurationModel.value.installationLocation.text,
                administrationConfig = administrationModel.value.toAdministrationConfig(),
                gameUserSettings = server.value.gameUserSettings.copy(
                    serverSettings = gameUserSettingsModel.value.toGameUserSettings().serverSettings
                )
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

    fun updateModsList(updatedMods: MutableList<Int>) {
        administrationModel.value = administrationModel.value.copy(mods = updatedMods)
    }
}
