package eu.wynq.arkascendedservermanager.core.server

import com.oblac.nomen.Casing
import com.oblac.nomen.Nomen
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.models.ServerEntity
import eu.wynq.arkascendedservermanager.core.db.repositories.SettingsRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ServerManager {
    fun createServer(): Result<Server> {
        val snakeCaseName = Nomen.est().adjective().noun().get()
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
}