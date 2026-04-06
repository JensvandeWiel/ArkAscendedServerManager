@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.repositories

import com.oblac.nomen.Nomen
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.models.ServerEntity
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.text.split
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ServersRepository {
    fun createServer(): Result<Server> {
        val snakeCaseName = Nomen.est().adjective().color().get()
        val name = snakeCaseName
            .split('_')
            .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }


        val dataPath = SettingsRepository.getDataPath().getOrElse {
            return Result.failure(it)
        }

        return runCatching {
            Server.fromEntity(transaction {
                ServerEntity.new {
                    profile_name = name
                    server_name = "$name Server hosted by JensvandeWiel/ArkAscendedServerManager"
                    installation_location = "$dataPath\\${snakeCaseName}"
                }
            })
        }

    }

    fun getServer(serverId: Uuid): Result<Server> = runCatching {
        transaction {
            ServerEntity.findById(serverId)!!.let(Server::fromEntity)
        }
    }

    fun getServers(): Result<List<Server>> = runCatching {
        transaction {
            ServerEntity.all().map { Server.fromEntity(it) }
        }
    }

    fun saveServer(server: Server) = runCatching {
        transaction {
            ServerEntity.findById(server.id)?.let { entity ->
                Server.applyToEntity(server, entity)
            }
        }
    }

    fun deleteServer(server: Server) = runCatching {
        transaction {
            ServerEntity.findById(server.id)?.delete()
        }
    }
}