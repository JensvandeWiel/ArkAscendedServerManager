@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.repositories

import com.oblac.nomen.Nomen
import db.models.Cluster
import db.models.ClusterEntity
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.models.ServerEntity
import eu.wynq.arkascendedservermanager.core.ini.Game
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.managers.InstallManager
import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.core.support.Constants
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Path
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
            val server = Server.fromEntity(transaction {
                ServerEntity.new {
                    profile_name = name
                    settings = Settings.createForNewServer()
                    installation_location = "$dataPath\\servers\\${snakeCaseName}"
                    asa_api = false
                    game_user_settings = GameUserSettings.createForNewServer(name)
                    game = Game.createForNewServer()
                }
            })
            server.saveGusToInstall().getOrThrow()
            server.saveGameToInstall().getOrThrow()
            InstallManager.createStartupScript(server).getOrThrow()
            server
        }

    }

    fun importServer(path: String): Result<Server> = runCatching {
        val installPath = Path.of(path).normalize()
        if (!installPath.toFile().exists()) {
            return Result.failure(IllegalArgumentException("Server installation path does not exist"))
        }

        val name = installPath.fileName.toString()

        var server = Server.fromEntity(transaction {
            ServerEntity.new {
                profile_name = name
                settings = Settings.createForNewServer()
                installation_location = path
                asa_api = false
                game_user_settings = GameUserSettings.createForNewServer(name)
                game = Game.createForNewServer()
            }
        })

        val existingGus = server.getGusFromInstall().getOrThrow()
        val existingGame = server.getGameFromInstall().getOrThrow()
        val existingSettings = server.getSettingsFromInstall().getOrThrow()
        val existingAsaApi = {
            val serverExecutable = Path.of(path, Constants.STARTUP_SCRIPT_PATH).toFile()
            if (serverExecutable.exists()) {
                serverExecutable.readText().contains("AsaApiLoader") ||
                        serverExecutable.readText().contains("Overseer")
            } else {
                false
            }
        }

        if (existingAsaApi()) {
            server = server.copy(asaApi = true)
        }

        if (existingGus != null) {
            server = server.copy(gameUserSettings = existingGus)
        } else {
            server.saveGusToInstall().getOrThrow()
        }

        if (existingGame != null) {
            server = server.copy(game = existingGame)
        } else {
            server.saveGameToInstall().getOrThrow()
        }

        if (existingSettings != null) {
            server = server.copy(settings = existingSettings)
        }

        val clusterUuid = server.settings.administration.clusterId?.let { Uuid.parseOrNull(it) }
        val existingCluster = transaction {
            clusterUuid?.let { ClusterEntity.findById(it) }
        }

        if (existingCluster != null) {
            server = server.copy(cluster = existingCluster.let { Cluster.fromEntity(it, includeServers = false) })
        }

        InstallManager.createStartupScript(server).getOrThrow()

        saveServer(server).getOrThrow()
        return@runCatching server
    }

    fun getServer(serverId: Uuid): Result<Server> = runCatching {
        transaction {
            ServerEntity.findById(serverId)!!.let(Server::fromEntity)
        }
    }

    fun getServers(): Result<List<Server>> = runCatching {
        transaction {
            ServerEntity.all().with(ServerEntity::cluster).map {
                val server = Server.fromEntity(it)
                val installGus = server.getGusFromInstall().getOrThrow()
                val installGame = server.getGameFromInstall().getOrThrow()
                var updatedServer = server
                if (installGus != null) {
                    updatedServer = updatedServer.copy(gameUserSettings = installGus)
                } else {
                    server.saveGusToInstall().getOrThrow()
                }
                if (installGame != null) {
                    updatedServer = updatedServer.copy(game = installGame)
                } else {
                    server.saveGameToInstall().getOrThrow()
                }
                if (updatedServer != server) {
                    saveServer(updatedServer).getOrThrow()
                }
                updatedServer
            }
        }
    }

    fun saveServer(server: Server) = runCatching {
        transaction {
            ServerEntity.findById(server.id)?.let { entity ->
                server.saveGusToInstall().getOrThrow()
                server.saveGameToInstall().getOrThrow()
                Server.applyToEntity(server, entity)
            }
        }
    }

    fun deleteServer(server: Server) = runCatching {
        transaction {
            ServerEntity.findById(server.id)?.delete()
        }
    }

    fun cloneServer(server: Server): Result<Server> = runCatching {
        val newProfileName = "${server.profileName} - Clone"
        val newInstallationLocation = "${server.installationLocation}_clone"

        val oldSettings = server.settings
        val newSettings = oldSettings.copy(
            administration = oldSettings.administration.copy(
                serverPort = oldSettings.administration.serverPort + 1,
                queryPort = oldSettings.administration.queryPort + 1,
                rconPort = oldSettings.administration.rconPort + 1
            )
        )

        val newServer = Server.fromEntity(transaction {
            ServerEntity.new {
                profile_name = newProfileName
                installation_location = newInstallationLocation
                asa_api = server.asaApi
                settings = newSettings
                game_user_settings = server.gameUserSettings.copy(
                    sessionSettings = server.gameUserSettings.sessionSettings.copy(
                        sessionName = newProfileName
                    )
                )
                game = server.game
                cluster = server.cluster?.let { ClusterEntity.findById(it.id) }
            }
        })

        newServer.saveGusToInstall().getOrThrow()
        newServer.saveGameToInstall().getOrThrow()
        InstallManager.createStartupScript(newServer).getOrThrow()
        newServer
    }
}