@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import eu.wynq.arkascendedservermanager.core.InstallManager
import eu.wynq.arkascendedservermanager.core.InstallStatus
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.support.AsaApiInstallManager
import eu.wynq.arkascendedservermanager.ui.stores.InstallStore
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class ServerDetailsTab {
    INFO,
    GENERAL,
}

class ServerComponent(
    componentContext: ComponentContext,
    private val serverId: Uuid,
) : ComponentContext by componentContext, KoinComponent {
    private val appScope: CoroutineScope by inject()
    private val serversStore: ServersStore by inject()
    private val installStore: InstallStore by inject()
    val installStatus: StateFlow<InstallStatus> = installStore.getProgress(serverId)
    private val _model = MutableValue(ServerModel())
    val model: Value<ServerModel> get() = _model
    val error: StateFlow<String?> = serversStore.error
    private val _selectedTab = MutableValue(ServerDetailsTab.INFO)
    val selectedTab: Value<ServerDetailsTab> get() = _selectedTab

    init {
        serversStore.getServer(serverId).onSuccess { loadedServer ->
            _model.update { it.copy(server = loadedServer, initialServer = loadedServer) }
            refreshInstallationInfo(loadedServer)
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

    fun saveServer() {
        _model.value.server?.let { currentServer ->
            serversStore.updateServer(currentServer)
            _model.update { state -> state.copy(initialServer = currentServer) }
            refreshInstallationInfo(currentServer)
        }
    }

    fun refreshInstallationInfo() {
        _model.value.initialServer?.let { refreshInstallationInfo(it) }
    }

    private fun refreshInstallationInfo(server: Server) {
        if (_model.value.initialServer != server) return

        appScope.launch {
            _model.update { state ->
                if (state.initialServer == server) {
                    state.copy(isInstalled = null, version = null)
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
                )
            }

            _model.update { state ->
                if (state.initialServer == server) {
                    state.copy(
                        isInstalled = installationInfo.isInstalled,
                        version = installationInfo.version,
                        apiIsInstalled = installationInfo.apiIsInstalled,
                        apiVersion = installationInfo.apiVersion,
                    )
                } else {
                    state
                }
            }
        }
    }

    fun updateServer(closure: (server: Server) -> Server) {
        _model.update { state ->
            state.server?.let { state.copy(server = closure(it)) } ?: state
        }
    }

    private data class ServerInstallationInfo(
        val isInstalled: Boolean,
        val apiIsInstalled: Boolean,
        val apiVersion: String?,
        val version: String?,
    )
}

