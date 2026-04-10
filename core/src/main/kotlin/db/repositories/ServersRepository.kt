@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.repositories

import com.oblac.nomen.Nomen
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.models.ServerEntity
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.server.Settings
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
                    settings = Settings.createForNewServer(name)
                    installation_location = "$dataPath\\${snakeCaseName}"
                    asa_api = false
                    game_user_settings = GameUserSettings()
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
            ServerEntity.all().map {
                val server = Server.fromEntity(it)
                val installGus = server.getGusFromInstall().getOrThrow()
                if (installGus != null) {
                    val server = server.copy(gameUserSettings = installGus)
                    saveServer(server).getOrThrow()
                    server
                } else {
                    server.saveGusToInstall().getOrThrow()
                    server
                }
            }
        }
    }

    fun saveServer(server: Server) = runCatching {
        transaction {
            ServerEntity.findById(server.id)?.let { entity ->
                server.saveGusToInstall().getOrThrow()
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