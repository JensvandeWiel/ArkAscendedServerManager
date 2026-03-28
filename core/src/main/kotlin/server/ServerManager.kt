package eu.wynq.arkascendedservermanager.core.server

import eu.wynq.arkascendedservermanager.core.db.ServerEntity
import io.github.oshai.kotlinlogging.KotlinLogging

class ServerManager {
    private val _logger = KotlinLogging.logger {}
    fun createServer() {
        val server = ServerEntity.Companion.new {
            profile_name = "Test"
            server_name = "Test"
            installation_location = "C:\\aasm\test"
        }

        _logger.info { "Created server: $server" }

    }
}