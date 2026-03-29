package eu.wynq.arkascendedservermanager.ui.features.servers

import com.arkivanov.decompose.ComponentContext
import eu.wynq.arkascendedservermanager.core.server.ServerManager
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ServersComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, KoinComponent {
    private val _logger = KotlinLogging.logger {}
    private val serverManager: ServerManager by inject()

    fun onServerAddClicked() {
        serverManager.createServer()
        _logger.info { "Add Server button clicked" }
    }
}