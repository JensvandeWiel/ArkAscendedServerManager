@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.stores

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.error_unknown
import arkascendedservermanager.ui.generated.resources.server_power_kill_failed
import arkascendedservermanager.ui.generated.resources.server_power_start_failed
import arkascendedservermanager.ui.generated.resources.server_power_stop_failed
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.managers.PowerManager
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PowerStore {
    fun getPowerState(server: Server): StateFlow<PowerState>
    fun startServer(server: Server)
    fun stopServer(server: Server)
    fun killServer(server: Server)
}

class PowerStoreImpl(private val appScope: CoroutineScope) : PowerStore {
    private val logger = KotlinLogging.logger {}
    private val powerStates = mutableMapOf<Uuid, MutableStateFlow<PowerState>>()
    private val servers = mutableMapOf<Uuid, Server>()
    private val pollingJobs = mutableMapOf<Uuid, Job>()

    override fun getPowerState(server: Server): StateFlow<PowerState> {
        val state = powerStates.getOrPut(server.id) { MutableStateFlow(PowerState.Unknown) }
        bindServer(server, state)
        return state.asStateFlow()
    }

    override fun startServer(server: Server) {
        executePowerOperation(server, PowerOperation.START, PowerManager::startServer, PowerState.Starting)
    }

    override fun stopServer(server: Server) {
        executePowerOperation(server, PowerOperation.STOP, PowerManager::stopServer, PowerState.Stopping)
    }

    override fun killServer(server: Server) {
        executePowerOperation(server, PowerOperation.KILL, PowerManager::killServer, PowerState.Stopped)
    }

    private fun bindServer(server: Server, state: MutableStateFlow<PowerState>) {
        val currentServer = servers[server.id]
        val pollingJob = pollingJobs[server.id]
        if (currentServer == server && pollingJob?.isActive == true) return

        pollingJob?.cancel()
        servers[server.id] = server

        pollingJobs[server.id] = appScope.launch {
            try {
                PowerManager.pollPowerState(server, oldStateProvider = { state.value }).collect { polledState ->
                    val oldState = state.value
                    val newState = mergePolledState(oldState, polledState)
                    state.value = newState

                    if (newState == PowerState.Crashed && server.settings.administration.restartAfterCrash) {
                        logger.info { "Server ${server.profileName} crashed, restarting in 5 seconds..." }
                        kotlinx.coroutines.delay(5000)
                        startServer(server)
                    }
                }
            } catch (t: Throwable) {
                logger.error(t) { "Power polling failed for ${server.profileName} (${server.id})" }
            }
        }

        appScope.launch(Dispatchers.IO) {
            try {
                state.value = PowerManager.getPowerState(server, state.value)
            } catch (t: Throwable) {
                logger.error(t) { "Failed to read power state for ${server.profileName} (${server.id})" }
            }
        }
    }

    private fun executePowerOperation(
        server: Server,
        operation: PowerOperation,
        action: suspend (Server) -> Result<Unit>,
        successState: PowerState,
    ) {
        val state = powerStates.getOrPut(server.id) { MutableStateFlow(PowerState.Unknown) }
        bindServer(server, state)

        appScope.launch {
            val result = action(server)

            if (result.isSuccess) {
                val oldState = state.value
                state.value = successState
                appScope.launch {
                    PowerManager.sendWebhook(server, oldState, successState).onFailure {
                        logger.error(it) { "Failed to send webhook for ${server.profileName} (${server.id}): ${it.message}" }
                    }
                }
                return@launch
            }

            state.value = withContext(Dispatchers.IO) { PowerManager.getPowerState(server, state.value) }
            if (result.isFailure) {
                logger.error(result.exceptionOrNull()) { "Power operation $operation failed for ${server.profileName} (${server.id})" }
                showPowerOperationFailedToast(operation, server, result.exceptionOrNull())
            }
        }
    }

    private suspend fun showPowerOperationFailedToast(
        operation: PowerOperation,
        server: Server,
        cause: Throwable?,
    ) {
        val reason = cause?.message?.takeIf { it.isNotBlank() } ?: getString(Res.string.error_unknown)
        val text = when (operation) {
            PowerOperation.START -> getString(Res.string.server_power_start_failed, server.profileName, reason)
            PowerOperation.STOP -> getString(Res.string.server_power_stop_failed, server.profileName, reason)
            PowerOperation.KILL -> getString(Res.string.server_power_kill_failed, server.profileName, reason)
        }

        withContext(Dispatchers.Main) {
            ToastBannerManager.show(
                type = ToastBannerType.ERROR,
                text = text,
                timeoutMillis = null,
            )
        }
    }

    private fun mergePolledState(currentState: PowerState, polledState: PowerState): PowerState {
        if (currentState == PowerState.Stopping && polledState == PowerState.Starting) {
            return PowerState.Stopping
        }

        return polledState
    }

    private enum class PowerOperation {
        START,
        STOP,
        KILL,
    }
}







