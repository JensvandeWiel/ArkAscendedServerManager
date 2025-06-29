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
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.dokar.sonner.ToastType
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
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

    private val navigation = StackNavigation<ServerConfig>()

    val stack: Value<ChildStack<*, ServerChild>> =
        childStack(
            source = navigation,
            serializer = ServerConfig.serializer(),
            initialConfiguration = ServerConfig.ProfileConfiguration,
            handleBackButton = false,
            key = "server_stack",
            childFactory = ::createServerChild
        )

    fun navigateToProfileConfiguration() {
        navigation.bringToFront(ServerConfig.ProfileConfiguration)
    }

    fun navigateToGeneralConfiguration() {
        navigation.bringToFront(ServerConfig.GeneralConfiguration)
    }

    val isProfileConfigurationActive: Value<Boolean> =
        stack.map { it.active.configuration == ServerConfig.ProfileConfiguration }
    val isGeneralConfigurationActive: Value<Boolean> = stack.map { it.active.configuration == ServerConfig.GeneralConfiguration }

    private fun createServerChild(
        config: ServerConfig,
        componentContext: ComponentContext
    ): ServerChild =
        when (config) {
            is ServerConfig.ProfileConfiguration -> ServerChild.ProfileConfiguration
            is ServerConfig.GeneralConfiguration -> ServerChild.GeneralConfiguration
        }

    sealed class ServerChild {
        data object ProfileConfiguration : ServerChild()
        data object GeneralConfiguration : ServerChild()
    }

    @Serializable
    private sealed class ServerConfig {
        @Serializable
        data object ProfileConfiguration : ServerConfig()

        @Serializable
        data object GeneralConfiguration : ServerConfig()
    }

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

    var profileConfigurationModel: MutableValue<ProfileConfigurationModel> = MutableValue(
        ProfileConfigurationModel.fromServerProfile(server.value)
    )

    val installationModel: MutableValue<InstallationModel> = MutableValue(
        InstallationModel(isInstalling = false, status = Preparing())
    )
    val generalConfigurationModel: MutableValue<GeneralConfigurationModel> =
        MutableValue(GeneralConfigurationModel.fromAdministrationConfig(server.value.administrationConfig))

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

            val administrationValidationResult = generalConfigurationModel.value.validate()
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
                administrationConfig = generalConfigurationModel.value.toAdministrationConfig(),
                gameUserSettings = server.value.gameUserSettings.copy(
                    serverSettings = gameUserSettingsModel.value.toGameUserSettings().serverSettings,
                    messageOfTheDay = gameUserSettingsModel.value.toGameUserSettings().messageOfTheDay
                )
            )

            println(updatedServer)

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
