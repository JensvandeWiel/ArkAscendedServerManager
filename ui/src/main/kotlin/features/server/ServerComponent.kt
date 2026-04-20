@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import db.models.Cluster
import eu.wynq.arkascendedservermanager.core.managers.InstallManager
import eu.wynq.arkascendedservermanager.core.managers.InstallStatus
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.server.Administration
import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.core.managers.AsaApiInstallManager
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.core.server.Options
import eu.wynq.arkascendedservermanager.core.support.Constants
import eu.wynq.arkascendedservermanager.core.support.watchFileContent
import eu.wynq.arkascendedservermanager.ui.stores.ClustersStore
import eu.wynq.arkascendedservermanager.ui.stores.InstallStore
import eu.wynq.arkascendedservermanager.ui.stores.PowerStore
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import eu.wynq.arkascendedservermanager.ui.stores.SettingsStore
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class ServerDetailsTab {
    INFO,
    GENERAL,
    PROFILE
}

class ServerComponent(
    componentContext: ComponentContext,
    private val onServerDeleted: (uuid: Uuid) -> Unit,
    private val serverId: Uuid,
) : ComponentContext by componentContext, KoinComponent {
    private val logger = KotlinLogging.logger {}
    private val appScope: CoroutineScope by inject()
    private val serversStore: ServersStore by inject()
    private val installStore: InstallStore by inject()
    private val powerStore: PowerStore by inject()
    private val clustersStore: ClustersStore by inject()
    private val settingsStore: SettingsStore by inject()
    val installStatus: StateFlow<InstallStatus> = installStore.getProgress(serverId)
    private val _model = MutableValue(ServerModel())
    val model: Value<ServerModel> get() = _model
    val error: StateFlow<String?> = serversStore.error
    val clusters = clustersStore.clusters
    private val _selectedTab = MutableValue(ServerDetailsTab.INFO)
    val selectedTab: Value<ServerDetailsTab> get() = _selectedTab
    private var _serverPowerState: StateFlow<PowerState> = MutableStateFlow(PowerState.Unknown)
    val serverPowerState: StateFlow<PowerState> get() = _serverPowerState
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs
    private var logJob: Job? = null

    private val _logger = KotlinLogging.logger {}

    init {
        serversStore.getServer(serverId).onSuccess { loadedServer ->
            _model.update { it.copy(server = loadedServer, initialServer = loadedServer) }
            refreshInstallationInfo(loadedServer)
            bindPowerState(loadedServer)
            if (loadedServer.asaApi) {
                startLogWatching(loadedServer)
            }
        }.onFailure {
            logger.warn(it) { "Server missing during component init: $serverId" }
        }
    }

    fun onDeletionDialogDismissed() {
        _model.update {
            it.copy(deleteDialogOpen = false)
        }
    }

    fun updateServerCluster(cluster: Cluster?) {
        if (cluster == null) {
            updateServer {
                it.copy(cluster = null)
            }
            updateServerAdministrationSettings {
                it.copy(clusterId = null, clusterDirOverride = null)
            }
        } else {
            val dataPath = settingsStore.settings.value?.dataPath

            updateServer {
                it.copy(cluster = cluster)
            }
            updateServerAdministrationSettings {
                it.copy(clusterDirOverride = dataPath, clusterId = cluster.id.toString())
            }
        }
    }

    fun onDeletionDialogConfirmed() {
        _model.update {
            it.copy(deleteDialogOpen = false)
        }
        val currentServer = model.value.server ?: run {
            logger.warn { "Delete confirmed without loaded server: $serverId" }
            return
        }
        serversStore.deleteServer(currentServer)
        onServerDeleted(serverId)
    }

    fun openDeleteDialog() {
        _model.update {
            it.copy(deleteDialogOpen = true)
        }
    }

    fun startInstall() {
        val currentServer = _model.value.server
        if (currentServer != null) {
            installStore.startInstall(currentServer)
        }
    }

    fun selectTab(tab: ServerDetailsTab) {
        _selectedTab.value = tab
    }

    fun selectInfoTab() {
        selectTab(ServerDetailsTab.INFO)
    }

    fun selectGeneralTab() {
        selectTab(ServerDetailsTab.GENERAL)
    }
    fun selectProfileTab() {
        selectTab(ServerDetailsTab.PROFILE)
    }

    fun saveServer() {
        _model.value.server?.let { currentServer ->
            serversStore.updateServer(currentServer)
            runCatching { InstallManager.createStartupScript(currentServer) }
                .onFailure { throwable ->
                    logger.error(throwable) { "Failed to create startup script for ${currentServer.profileName} (${currentServer.id})" }
                }

            if (currentServer.cluster != _model.value.initialServer?.cluster) {
                logger.info { "Server ${currentServer.profileName} (${currentServer.id}) cluster changed, refreshing cluster list" }
                clustersStore.loadClusters()
            }

            _model.update { state -> state.copy(initialServer = currentServer) }
            refreshInstallationInfo(currentServer)
            bindPowerState(currentServer)
        }
    }

    fun startServer() {
        _model.value.server?.let(powerStore::startServer)
    }

    fun stopServer() {
        _model.value.server?.let(powerStore::stopServer)
    }

    fun killServer() {
        _model.value.server?.let(powerStore::killServer)
    }

    fun refreshInstallationInfo() {
        _model.value.initialServer?.let { refreshInstallationInfo(it) }
    }

    private fun refreshInstallationInfo(server: Server) {
        if (_model.value.initialServer != server) return

        appScope.launch {
            try {
                _model.update { state ->
                    if (state.initialServer == server) {
                        state.copy(
                            isInstalled = null,
                            version = null,
                            apiVersion = null,
                            apiIsInstalled = null,
                            isOverseerInstalled = null,
                            overseerVersion = null,
                        )
                    } else {
                        state
                    }
                }

                val installationInfo = withContext(Dispatchers.IO) {
                    ServerInstallationInfo(
                        isInstalled = InstallManager.isInstalled(server),
                        version = InstallManager.getServerVersion(server),
                        apiVersion = if (server.asaApi) AsaApiInstallManager.getApiVersionAsString(server) else null,
                        apiIsInstalled = AsaApiInstallManager.isInstalled(server),
                        overseerIsInstalled = AsaApiInstallManager.isOverseerInstalled(server),
                        overseerVersion = AsaApiInstallManager.getOverseerVersionAsString(server),
                    )
                }

                _model.update { state ->
                    if (state.initialServer == server) {
                        state.copy(
                            isInstalled = installationInfo.isInstalled,
                            version = installationInfo.version,
                            apiIsInstalled = installationInfo.apiIsInstalled,
                            apiVersion = installationInfo.apiVersion,
                            isOverseerInstalled = installationInfo.overseerIsInstalled,
                            overseerVersion = installationInfo.overseerVersion,
                        )
                    } else {
                        state
                    }
                }
            } catch (t: Throwable) {
                logger.error(t) { "Failed to refresh installation info for ${server.profileName} (${server.id})" }
            }
        }
    }

    fun updateServer(closure: (server: Server) -> Server) {
        _model.update { state ->
            state.server?.let { state.copy(server = closure(it)) } ?: state
        }
    }

    fun updateServerSettings(closure: (server: Settings) -> Settings) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(settings = closure(state.server.settings))) } ?: state
        }
    }

    fun updateServerAdministrationSettings(closure: (administration: Administration) -> Administration) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(settings = state.server.settings.copy(administration = closure(state.server.settings.administration)))) } ?: state
        }
    }

    fun updateServerOptions(closure: (options: Options) -> Options) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(settings = state.server.settings.copy(options = closure(state.server.settings.options)))) } ?: state
        }
    }

    fun updateServerGameUserSettings(closure: (server: GameUserSettings) -> GameUserSettings) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(gameUserSettings = closure(state.server.gameUserSettings))) } ?: state
        }
    }

    private fun bindPowerState(server: Server) {
        _serverPowerState = powerStore.getPowerState(server)
    }

    private fun startLogWatching(server: Server) {
        logJob?.cancel()
        logJob = appScope.launch {
            try {
                val logFile = java.io.File(server.installationLocation, Constants.OVERSEER_SERVER_LOG_PATH)
                _logger.info { "Starting log watcher for ${server.profileName} (${server.id} at ${logFile.absolutePath})" }
                watchFileContent(logFile).collect { line ->
                    _logs.update { (it + line).takeLast(100) }
                }
            } catch (t: Throwable) {
                logger.error(t) { "Log watcher failed for ${server.profileName} (${server.id}): ${t.message}" }
            }
        }
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }

    private data class ServerInstallationInfo(
        val isInstalled: Boolean,
        val apiIsInstalled: Boolean,
        val apiVersion: String?,
        val overseerIsInstalled: Boolean,
        val overseerVersion: String?,
        val version: String?,
    )

}
