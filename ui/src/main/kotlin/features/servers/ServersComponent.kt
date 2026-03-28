package eu.wynq.arkascendedservermanager.ui.features.servers

import com.arkivanov.decompose.ComponentContext
import io.github.oshai.kotlinlogging.KotlinLogging

class ServersComponent(componentContext: ComponentContext) : ComponentContext by componentContext {
    private val _logger = KotlinLogging.logger {}

    public fun onServerAddClicked() {
        _logger.info { "Add Server button clicked" }
    }
}