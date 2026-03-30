@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.stores

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.repositories.ServersRepository
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
    fun loadServer(serverId: Uuid): Server?
    fun deleteServer(server: Server)
    fun updateServer(server: Server)
}


class ServersStoreImpl: ServersStore {
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
            _error.value = newServer.exceptionOrNull()?.message
            null
        }
    }

    override fun loadServer(serverId: Uuid): Server? {
        val dbServer = ServersRepository.getServer(serverId)
        return if (dbServer.isSuccess) {
            val loadedServer = dbServer.getOrThrow()
            if (loadedServer != null) {
                _servers.value = _servers.value + (loadedServer.id to loadedServer)
            }
            _error.value = null
            loadedServer
        } else {
            _error.value = dbServer.exceptionOrNull()?.message
            null
        }
    }

    override fun deleteServer(server: Server) {
        ServersRepository.deleteServer(server).onSuccess {
            _servers.value = _servers.value - server.id
        }
    }

    override fun updateServer(server: Server) {
        ServersRepository.saveServer(server).onSuccess {
            _servers.value = _servers.value + (server.id to server)
        }
    }
}