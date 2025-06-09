@file:OptIn(ExperimentalUuidApi::class)
package server

import com.oblac.nomen.Nomen
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import settings.SettingsHelper
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ProfileLoader {
    private val logger = KotlinLogging.logger {}
    fun loadProfiles(dataDir: String = SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: "C:/aasm"): Result<List<ServerProfile>> {
        val dataDirPath = Path.of(dataDir).resolve("servers")

        return try {
            val servers = dataDirPath.toFile().listFiles()?.mapNotNull { file ->
                if (file.isFile && file.extension == "json") {
                    val json = file.readText()
                    Json {
                        prettyPrint = true
                        encodeDefaults = true
                    }.decodeFromString<ServerProfile>(json)
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

    fun generateNewProfile(): ServerProfile {

        val name = Nomen.est().adjective().color().get()

        return ServerProfile(
            uuid = Uuid.random(),
            profileName = name,
            installationLocation = Path.of(SettingsHelper().getSettings().getOrNull()?.applicationDataPath!!).resolve("servers").resolve(name).toString(),
            administrationConfig = AdministrationConfig(serverName = name)
        )
    }

    fun addProfile(dataDir: String = SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: "C:/aasm", profile: ServerProfile): Result<Unit> {
        return try {
            val dataDirPath = Path.of(dataDir).resolve("servers")
            dataDirPath.toFile().mkdirs()

            val serverFile = dataDirPath.resolve("${profile.uuid}.json").toFile()
            serverFile.writeText(Json.encodeToString(profile))
            logger.info { "Added new server: ${profile.profileName} with UUID: ${profile.uuid}" }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateProfile(dataDir: String = SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: "C:/aasm", profile: ServerProfile): Result<Unit> {
        return try {
            val dataDirPath = Path.of(dataDir).resolve("servers")
            val serverFile = dataDirPath.resolve("${profile.uuid}.json").toFile()
            if (serverFile.exists()) {
                serverFile.writeText(Json {
                    prettyPrint = true
                    encodeDefaults = true

                }.encodeToString(profile))
                logger.info { "Updated server: ${profile.profileName} with UUID: ${profile.uuid}" }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server file does not exist: ${serverFile.absolutePath}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}