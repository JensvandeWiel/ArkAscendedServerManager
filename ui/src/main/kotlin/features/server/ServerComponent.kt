@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import com.arkivanov.decompose.ComponentContext
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class ServerDetailsTab {
    INFO,
    PROFILE,
}

class ServerComponent(
    componentContext: ComponentContext,
    private val serverId: Uuid,
) : ComponentContext by componentContext, KoinComponent {
    private val serversStore: ServersStore by inject()

    private val _server = MutableStateFlow<Server?>(null)
    val server: StateFlow<Server?> = _server.asStateFlow()
    val error: StateFlow<String?> = serversStore.error

    private val _selectedTab = MutableStateFlow(ServerDetailsTab.INFO)
    val selectedTab: StateFlow<ServerDetailsTab> = _selectedTab.asStateFlow()

    init {
        loadServer()
    }

    fun loadServer() {
        _server.value = serversStore.loadServer(serverId)
    }

    fun selectTab(tab: ServerDetailsTab) {
        _selectedTab.value = tab
    }

    fun selectInfoTab() {
        selectTab(ServerDetailsTab.INFO)
    }
    fun selectProfileTab() {
        selectTab(ServerDetailsTab.PROFILE)
    }
}

