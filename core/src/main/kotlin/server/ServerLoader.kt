package server

import com.oblac.nomen.Nomen
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import settings.SettingsHelper
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object ServerLoader {
    private val logger = KotlinLogging.logger {}
    fun loadServers(dataDir: String = SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: "C:/aasm"): Result<List<ServerConfig>> {
        val dataDirPath = Path.of(dataDir).resolve("servers")

        return try {
            val servers = dataDirPath.toFile().listFiles()?.mapNotNull { file ->
                if (file.isFile && file.extension == "json") {
                    val json = file.readText()
                    Json.decodeFromString<ServerConfig>(json)
                } else {
                    null
                }
            } ?: emptyList()
            logger.info { "Loaded ${servers.size} servers from $dataDirPath" }
            Result.success(servers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun generateNewServer(): ServerConfig {
        return ServerConfig(
            uuid = Uuid.random(),
            profileName = Nomen.est().adjective().color().person().get()
        )
    }

    fun addServer(dataDir: String = SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: "C:/aasm", serverConfig: ServerConfig): Result<Unit> {
        return try {
            val dataDirPath = Path.of(dataDir).resolve("servers")
            dataDirPath.toFile().mkdirs()

            val serverFile = dataDirPath.resolve("${serverConfig.uuid}.json").toFile()
            serverFile.writeText(Json.encodeToString(serverConfig))
            logger.info { "Added new server: ${serverConfig.profileName} with UUID: ${serverConfig.uuid}" }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}