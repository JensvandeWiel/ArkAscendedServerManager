@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.stores

import SteamCMD
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_install_error_steamcmd_path_not_configured
import arkascendedservermanager.ui.generated.resources.server_install_toast_failed
import arkascendedservermanager.ui.generated.resources.server_install_toast_success
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.managers.Idle
import eu.wynq.arkascendedservermanager.core.managers.InstallDone
import eu.wynq.arkascendedservermanager.core.managers.InstallError
import eu.wynq.arkascendedservermanager.core.managers.InstallManager
import eu.wynq.arkascendedservermanager.core.managers.InstallStatus
import eu.wynq.arkascendedservermanager.core.managers.InstallingGame
import eu.wynq.arkascendedservermanager.core.managers.Preparing
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class InstallStore(private val settingsStore: SettingsStore, private val appScope: CoroutineScope) {
    private val _state: MutableMap<Uuid, MutableStateFlow<InstallStatus>> = mutableMapOf()

    private val _logger = KotlinLogging.logger {}

    private val steamCMD: SteamCMD?
        get() = settingsStore.settings.value?.let { SteamCMD(Path.of(it.steamcmdPath)) }

    /**
     * Gets the current progress for a server.
     * Returns a StateFlow that can be collected by the UI.
     */
    fun getProgress(serverId: Uuid): StateFlow<InstallStatus> {
        return _state.getOrPut(serverId) {
            MutableStateFlow(Idle())
        }
    }

    /**
     * Starts the installation process in the application-level scope.
     */
    fun startInstall(server: Server) {
        val currentSteamCMD = steamCMD ?: run {
            _state[server.id]?.value = InstallError(
                runBlocking { getString(Res.string.server_install_error_steamcmd_path_not_configured) }
            )
            return
        }

        val state = _state.getOrPut(server.id) { MutableStateFlow(Idle()) }
        if (state.value !is Idle && state.value !is InstallError && state.value !is InstallDone) return

        appScope.launch {
            runAsFlow(server, currentSteamCMD)
                .flowOn(Dispatchers.IO)
                .collect { status ->
                    state.value = status
                }
        }
    }

    private fun runAsFlow(server: Server, steamCMD: SteamCMD): Flow<InstallStatus> = flow {
        emit(Preparing())
        InstallManager.install(server, steamCMD).collect { status ->
            when (status) {
                is InstallDone -> {
                    withContext(Dispatchers.Main) {
                        ToastBannerManager.show(
                            ToastBannerType.SUCCESS,
                            getString(Res.string.server_install_toast_success, server.profileName),
                        )
                    }
                    emit(status)
                    _logger.info { "Installation status update for server ${server.profileName} (${server.id}): Done" }
                }
                is InstallError -> {
                    withContext(Dispatchers.Main) {
                        ToastBannerManager.show(
                            ToastBannerType.ERROR,
                            getString(Res.string.server_install_toast_failed, server.profileName, status.error),
                            timeoutMillis = null,
                        )
                    }
                    emit(Idle())
                    _logger.info { "Installation status update for server ${server.profileName} (${server.id}): Error: ${status.error}" }
                }
                is InstallingGame -> {
                    emit(status)
                    _logger.info { "Installation status update for server ${server.profileName} (${server.id}): Installing Game - ${status.status}" }
                }
                else -> {
                    emit(status)
                    _logger.info { "Installation status update for server ${server.profileName} (${server.id}): ${status::class.simpleName ?: "Unknown"}" }
                }
            }
        }
    }
}