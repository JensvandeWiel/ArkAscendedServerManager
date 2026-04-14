@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.stores

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.repositories.ServersRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ServersStore {
    val servers: StateFlow<Map<Uuid, Server>>
    val error: StateFlow<String?>
    fun loadServers()
    fun createServer(): Server?
    fun getServer(serverId: Uuid): Result<Server>
    fun deleteServer(server: Server)
    fun updateServer(server: Server)
    fun importServer(path: String): Result<Server>
}


class ServersStoreImpl: ServersStore {
    private val logger = KotlinLogging.logger {}
    private val _servers = MutableStateFlow<Map<Uuid, Server>>(emptyMap())
    override val servers: StateFlow<Map<Uuid, Server>> = _servers.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    override val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadServers()
    }

    override fun loadServers() {
        val dbServers = ServersRepository.getServers()
        if (dbServers.isSuccess) {
            _servers.value = dbServers.getOrThrow().associateBy { it.id }
            _error.value = null
        } else {
            logger.error(dbServers.exceptionOrNull()) { "Failed to load servers" }
            _error.value = dbServers.exceptionOrNull()?.message
        }
    }

    override fun createServer(): Server? {
        val newServer = ServersRepository.createServer()
        return if (newServer.isSuccess) {
            val createdServer = newServer.getOrThrow()
            _servers.value = _servers.value + (createdServer.id to createdServer)
            _error.value = null
            createdServer
        } else {
            logger.error(newServer.exceptionOrNull()) { "Failed to create server" }
            _error.value = newServer.exceptionOrNull()?.message
            null
        }
    }

    override fun importServer(path: String): Result<Server> {
        val importedServer = ServersRepository.importServer(path)
        return if (importedServer.isSuccess) {
            _servers.value = _servers.value + (importedServer.getOrThrow().id to importedServer.getOrThrow())
            Result.success(importedServer.getOrThrow())
        } else {
            logger.error(importedServer.exceptionOrNull()) { "Failed to import server from path $path" }
            _error.value = importedServer.exceptionOrNull()?.message
            Result.failure(importedServer.exceptionOrNull() ?: IllegalStateException("Unknown error during server import"))
        }
    }

    override fun getServer(serverId: Uuid): Result<Server> {
        servers.value[serverId]?.let { return Result.success(it) }
        logger.warn { "Server not found: $serverId" }
        return Result.failure(IllegalStateException("Server not found"))
    }

    override fun deleteServer(server: Server) {
        ServersRepository.deleteServer(server).onSuccess {
            _servers.value = _servers.value - server.id
        }.onFailure {
            logger.error(it) { "Failed to delete server ${server.profileName} (${server.id})" }
        }
    }

    override fun updateServer(server: Server) {
        ServersRepository.saveServer(server).onSuccess {
            _servers.value = _servers.value + (server.id to server)
        }.onFailure {
            logger.error(it) { "Failed to save server ${server.profileName} (${server.id})" }
        }
    }
}