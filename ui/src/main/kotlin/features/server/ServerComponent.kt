@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import kotlinx.coroutines.flow.StateFlow
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
    private val serversStore: ServersStore by inject()

    private val _model: MutableValue<ServerModel>
    val model: Value<ServerModel> get() = _model

    val error: StateFlow<String?> = serversStore.error

    private val _selectedTab = MutableValue(ServerDetailsTab.INFO)
    val selectedTab: Value<ServerDetailsTab> get() = _selectedTab

    init {
        _model = MutableValue(ServerModel())
        _selectedTab.value = ServerDetailsTab.INFO
        serversStore.getServer(serverId).onSuccess {
            _model.value = ServerModel(server = it, initialServer = it)
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
        _model.value.server?.let {
            serversStore.updateServer(it)
            _model.value = _model.value.copy(initialServer = it)
        }
    }

    fun updateServer(closure: (server: Server) -> Server) {
        val currentServer = _model.value.server
        if (currentServer != null) {
            val updatedServer = closure(currentServer)
            _model.value = _model.value.copy(server = updatedServer)
        }
    }
}

