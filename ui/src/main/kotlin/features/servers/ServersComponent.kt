@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.servers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.managers.InstallStatus
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.ui.stores.InstallStore
import eu.wynq.arkascendedservermanager.ui.stores.PowerStore
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ServersComponent(
    componentContext: ComponentContext,
    private val onOpenServer: (Uuid) -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    private val _logger = KotlinLogging.logger {}
    private val serversStore: ServersStore by inject()
    private val powerStore: PowerStore by inject()
    private val installStore: InstallStore by inject()
    private val _model: MutableValue<ServersModel> = MutableValue(ServersModel())
    val model: Value<ServersModel> get() = _model

    val servers = serversStore.servers

    fun getPowerState(server: Server): StateFlow<PowerState> = powerStore.getPowerState(server)

    fun getInstallStatus(server: Server): StateFlow<InstallStatus> = installStore.getProgress(server.id)

    fun startServer(server: Server) = powerStore.startServer(server)

    fun stopServer(server: Server) = powerStore.stopServer(server)

    fun killServer(server: Server) = powerStore.killServer(server)

    fun onServerAddClicked() {
        val server = serversStore.createServer()
        if (server != null) {
            onOpenServer(server.id)
            _logger.info { "Created server and opened page: ${server.profileName} (${server.id})" }
        }
    }

    fun onServerClicked(server: Server) {
        onOpenServer(server.id)
        _logger.info { "Server card clicked: ${server.profileName} (${server.id})" }
    }

    fun onImportServerClicked() {
        _model.update {
            it.copy(
                importServersDialogOpen = true
            )
        }
    }

    fun onImportServersDialogDismissed() {
        _model.update {
            it.copy(
                importServersDialogOpen = false
            )
        }
    }

    fun importServer(path: String) {
        _model.update {
            it.copy(
                importServersDialogOpen = false
            )
        }
        val server = serversStore.importServer(path)
        if (server.isFailure) return _logger.error(server.exceptionOrNull()) {
            "Failed to import server from path $path"
        }
        onOpenServer(server.getOrThrow().id)
    }
}