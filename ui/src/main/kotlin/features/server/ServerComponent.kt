@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import db.models.Cluster
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.ini.Game
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.managers.AsaApiInstallManager
import eu.wynq.arkascendedservermanager.core.managers.InstallManager
import eu.wynq.arkascendedservermanager.core.managers.InstallStatus
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.core.server.Administration
import eu.wynq.arkascendedservermanager.core.server.Options
import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.core.support.Constants
import eu.wynq.arkascendedservermanager.core.support.watchFileContent
import eu.wynq.arkascendedservermanager.ui.stores.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Desktop
import java.io.File
import java.io.RandomAccessFile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class ServerDetailsTab {
    INFO,
    GENERAL,
    PROFILE,
    MANAGEMENT,
    ENVIRONMENT,
    RULES,
    CONFIG_EDITOR,
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
    private var logReloadJob: Job? = null
    private var currentLogFile: File? = null
    private val logsMutex = Mutex()

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

    fun selectManagementTab() {
        selectTab(ServerDetailsTab.MANAGEMENT)
    }

    fun selectConfigEditorTab() {
        selectTab(ServerDetailsTab.CONFIG_EDITOR)
    }

    fun selectEnvironmentTab() {
        selectTab(ServerDetailsTab.ENVIRONMENT)
    }

    fun selectRulesTab() {
        selectTab(ServerDetailsTab.RULES)
    }

    fun selectProfileTab() {
        selectTab(ServerDetailsTab.PROFILE)
    }

    suspend fun checkUpdateAvailable() {
        val res = InstallManager.canUpdate(_model.value.server ?: return)
        if (res.isFailure) {
            logger.error(res.exceptionOrNull()) { "Failed to check for update for ${_model.value.server?.profileName} (${_model.value.server?.id})" }
            return
        }
        try {
            _model.update { state ->
                if (state.server == null) {
                    state
                } else {
                    state.copy(hasUpdateAvailable = if (res.getOrNull() == true) UpdateStatus.Available else UpdateStatus.UpToDate)
                }
            }
        } catch (t: Throwable) {
            logger.error(t) { "Failed to update model with update check result for ${_model.value.server?.profileName} (${_model.value.server?.id})" }
        }
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
            if (currentServer.asaApi) {
                startLogWatching(currentServer)
            } else {
                logJob?.cancel()
                _logs.value = emptyList()
            }
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

    fun refreshInfo() = appScope.launch {
        refreshInstallationInfo()
        checkUpdateAvailable()
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

    fun openServerDir(): Result<Unit> = runCatching {
        Desktop.getDesktop().open(java.io.File(model.value.server?.installationLocation ?: return Result.failure(IllegalStateException("Server not loaded"))))
    }

    fun updateServer(closure: (server: Server) -> Server) {
        _model.update { state ->
            state.server?.let { state.copy(server = closure(it)) } ?: state
        }
    }

    fun updateServerSettings(closure: (server: Settings) -> Settings) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(settings = closure(state.server.settings))) }
                ?: state
        }
    }

    fun updateServerAdministrationSettings(closure: (administration: Administration) -> Administration) {
        _model.update { state ->
            state.server?.let {
                state.copy(
                    server = state.server.copy(
                        settings = state.server.settings.copy(
                            administration = closure(state.server.settings.administration)
                        )
                    )
                )
            } ?: state
        }
    }

    fun updateServerOptions(closure: (options: Options) -> Options) {
        _model.update { state ->
            state.server?.let {
                state.copy(
                    server = state.server.copy(
                        settings = state.server.settings.copy(
                            options = closure(
                                state.server.settings.options
                            )
                        )
                    )
                )
            } ?: state
        }
    }

    fun updateServerGameUserSettings(closure: (server: GameUserSettings) -> GameUserSettings) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(gameUserSettings = closure(state.server.gameUserSettings))) }
                ?: state
        }
    }

    fun updateServerGame(closure: (game: Game) -> Game) {
        _model.update { state ->
            state.server?.let { state.copy(server = state.server.copy(game = closure(state.server.game))) } ?: state
        }
    }

    private fun bindPowerState(server: Server) {
        _serverPowerState = powerStore.getPowerState(server)
    }

    private fun startLogWatching(server: Server) {
        logJob?.cancel()
        logJob = appScope.launch {
            try {
                val logFile = File(server.installationLocation, Constants.OVERSEER_SERVER_LOG_PATH)
                currentLogFile = logFile
                _logger.info { "Starting log watcher for ${server.profileName} (${server.id} at ${logFile.absolutePath})" }
                watchFileContent(logFile).collect { line ->
                    logsMutex.withLock {
                        _logs.value = (_logs.value + line).takeLast(_model.value.logLineLimit)
                    }
                }
            } catch (t: Throwable) {
                logger.error(t) { "Log watcher failed for ${server.profileName} (${server.id}): ${t.message}" }
            }
        }
    }

    fun updateLogLineLimitInput(input: String) {
        val limit = input.toIntOrNull()?.takeIf { it > 0 }
        val previousLimit = _model.value.logLineLimit
        _model.update { state ->
            state.copy(
                logLineLimit = limit ?: state.logLineLimit,
                logLineLimitInput = input,
            )
        }

        if (limit == null || limit == previousLimit) return

        if (_serverPowerState.value == PowerState.Stopped) {
            _logs.value = emptyList()
            return
        }
        reloadLogsFromFile(limit)
    }

    private fun reloadLogsFromFile(limit: Int) {
        val logFile = currentLogFile
        logReloadJob?.cancel()
        logReloadJob = appScope.launch(Dispatchers.IO) {
            val lines = if (logFile == null || !logFile.exists()) {
                _logs.value.takeLast(limit)
            } else {
                readLastLines(logFile, limit)
            }
            logsMutex.withLock {
                _logs.value = lines
            }
        }
    }

    private fun readLastLines(file: File, maxLines: Int): List<String> {
        if (maxLines <= 0 || !file.exists()) return emptyList()

        RandomAccessFile(file, "r").use { raf ->
            val fileLength = raf.length()
            if (fileLength == 0L) return emptyList()

            val buffer = ByteArray(8192)
            var position = fileLength
            var start = 0L
            var lineBreaks = 0

            while (position > 0 && lineBreaks <= maxLines) {
                val readSize = minOf(buffer.size.toLong(), position).toInt()
                position -= readSize
                raf.seek(position)
                raf.readFully(buffer, 0, readSize)

                for (index in readSize - 1 downTo 0) {
                    if (buffer[index] == '\n'.code.toByte()) {
                        lineBreaks++
                        if (lineBreaks > maxLines) {
                            start = position + index + 1
                            position = 0
                            break
                        }
                    }
                }
            }

            raf.seek(start)
            val bytes = ByteArray((fileLength - start).toInt())
            raf.readFully(bytes)
            return String(bytes, Charsets.UTF_8)
                .lines()
                .dropLastWhile { it.isEmpty() }
                .takeLast(maxLines)
        }
    }

    fun clearLogs() {
        logReloadJob?.cancel()
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
