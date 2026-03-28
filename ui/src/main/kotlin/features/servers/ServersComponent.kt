package eu.wynq.arkascendedservermanager.ui.features.servers

import com.arkivanov.decompose.ComponentContext
import eu.wynq.arkascendedservermanager.core.server.ServerManager
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ServersComponent(componentContext: ComponentContext) : ComponentContext by componentContext {
    private val _logger = KotlinLogging.logger {}

    public fun onServerAddClicked() {
        val serverManager = ServerManager()
        transaction {
            serverManager.createServer()
        }
        _logger.info { "Add Server button clicked" }
    }
}