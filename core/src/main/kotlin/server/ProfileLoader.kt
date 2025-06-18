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
    private val defaultDataDir: String by lazy {
        SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: DEFAULT_DATA_DIR
    }

    private fun getServerDir(dataDir: String): Path = Path.of(dataDir).resolve("servers")

    fun loadProfiles(
        dataDir: String = defaultDataDir
    ): Result<List<ServerProfile>> {
        val dataDirPath = getServerDir(dataDir)

        return try {
            val servers = dataDirPath.toFile().listFiles()?.mapNotNull { file ->
                if (file.isFile && file.extension == "json") {
                    val json = file.readText()
                    var profile = Json {
                        prettyPrint = true
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                    }.decodeFromString<ServerProfile>(json)

                    var gus = profile.loadGameUserSettings().getOrThrow()
                    if (gus != null) {
                        profile = profile.copy(
                            gameUserSettings = gus
                        )
                    }

                    if (gus == null) {
                        profile.saveGameUserSettings().getOrThrow()
                    }

                    profile
                } else {
                    null
                }
            } ?: emptyList()
            logger.info { "Loaded ${servers.size} servers from $dataDirPath" }
            Result.success(servers)
        } catch (e: Exception) {
            logger.error(e) { "Failed to load server profiles from $dataDirPath: " + e.message }
            Result.failure(e)
        }
    }

    fun generateNewProfile(): ServerProfile {
        val name = Nomen.est().adjective().color().get()
        val appDataPath = SettingsHelper().getSettings().getOrNull()?.applicationDataPath ?: DEFAULT_DATA_DIR
        return ServerProfile(
            uuid = Uuid.random(),
            profileName = name,
            installationLocation = Path.of(appDataPath)
                .resolve("servers").resolve(name).toString(),
            administrationConfig = AdministrationConfig(serverName = name)
        )
    }

    fun addProfile(
        dataDir: String = defaultDataDir,
        profile: ServerProfile
    ): Result<Unit> {
        return try {
            val dataDirPath = getServerDir(dataDir)
            dataDirPath.toFile().mkdirs()

            val serverFile = dataDirPath.resolve("${profile.uuid}.json").toFile()
            serverFile.writeText(Json.encodeToString(profile))
            createServerStartupScript(profile)
            logger.info { "Added new server: ${profile.profileName} with UUID: ${profile.uuid}" }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun createServerStartupScript(profile: ServerProfile): Result<Unit> {
        logger.info { "Creating startup script for server: ${profile.profileName} at ${profile.installationLocation}" }
        val script = profile.generateStartupScript()
        val scriptFile = Path.of(profile.installationLocation).resolve("start.bat").toFile()
        // Ensure the directory exists
        scriptFile.parentFile?.mkdirs()
        return try {
            scriptFile.writeText(script)
            logger.info { "Created startup script for server: ${profile.profileName} at ${scriptFile.absolutePath}" }
            Result.success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Failed to create startup script for server: ${profile.profileName}" }
            Result.failure(e)
        }
    }

    fun updateStartupScript(profile: ServerProfile): Result<Unit> {
        val script = profile.generateStartupScript()
        val scriptFile = Path.of(profile.installationLocation).resolve("start.bat").toFile()
        // Ensure the directory exists
        scriptFile.parentFile?.mkdirs()
        logger.info { "Updating startup script for server: ${profile.profileName} at ${scriptFile.absolutePath}" }
        return try {
            if (scriptFile.exists()) {
                scriptFile.writeText(script)
                logger.info { "Updated startup script for server: ${profile.profileName}" }
                Result.success(Unit)
            } else {
                logger.warn { "Startup script does not exist for server: ${profile.profileName}, creating new one." }
                return createServerStartupScript(profile)
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to update startup script for server: ${profile.profileName}" }
            Result.failure(e)
        }
    }

    fun updateProfile(
        dataDir: String = defaultDataDir,
        profile: ServerProfile
    ): Result<Unit> {
        return try {
            val dataDirPath = getServerDir(dataDir)
            val serverFile = dataDirPath.resolve("${profile.uuid}.json").toFile()
            if (serverFile.exists()) {
                serverFile.writeText(Json {
                    prettyPrint = true
                    encodeDefaults = true
                }.encodeToString(profile))
                logger.info { "Updating GameUserSettings: ${profile.profileName} with UUID: ${profile.uuid}" }
                profile.saveGameUserSettings().getOrThrow()
                logger.info { "Updated GameUserSettings for server: ${profile.profileName}" }
                logger.info { "Updating startup script: ${profile.profileName} with UUID: ${profile.uuid}" }
                updateStartupScript(profile).getOrThrow()
                logger.info { "Updated startup script for server: ${profile.profileName} with UUID: ${profile.uuid}" }
                logger.info { "Updated server: ${profile.profileName} with UUID: ${profile.uuid}" }
                return Result.success(Unit)
            } else {
                Result.failure(Exception("Server file does not exist: ${serverFile.absolutePath}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private const val DEFAULT_DATA_DIR = "C:/aasm"
}