@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.servers

import com.arkivanov.decompose.ComponentContext
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import io.github.oshai.kotlinlogging.KotlinLogging
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
    
    val servers = serversStore.servers

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
}