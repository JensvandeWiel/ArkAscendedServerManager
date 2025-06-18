package server

import kotlinx.serialization.Serializable
import serialization.IniSerializer
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class ServerProfile @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    val profileName: String,
    val installationLocation: String,
    val administrationConfig: AdministrationConfig = AdministrationConfig(serverName = profileName),
    val gameUserSettings: GameUserSettings = GameUserSettings()
) {
    fun getServerManager(): ServerManager {
        return ServerManager(this)
    }

    fun generateStartupScript(): String {
        val sb = StringBuilder()
        sb.append("start ")
        sb.append(Path.of(installationLocation, Constants.SERVER_BINARY_PATH, Constants.SERVER_EXECUTABLE_NAME))
        sb.append(" ")
        sb.append(administrationConfig.map)
        sb.append("?SessionName=${administrationConfig.serverName}")
        sb.append("?Port=${administrationConfig.serverPort}")
        sb.append("?QueryPort=${administrationConfig.queryPort}")
        if (administrationConfig.rconEnabled) {
            sb.append("?RCONEnabled=True")
            sb.append("?RCONPort=${administrationConfig.rconPort}")
        }
        if (!administrationConfig.serverPassword.isNullOrBlank()) {
            sb.append("?ServerPassword=${administrationConfig.serverPassword}")
        }
        sb.append("?ServerAdminPassword=${administrationConfig.adminPassword}")
        sb.append(" -WinLiveMaxPlayers=" + administrationConfig.slots)
        if (administrationConfig.mods.isNotEmpty()) {
            sb.append(" -mods=" + administrationConfig.mods.joinToString(","))
        }
        // sb.append(" -OldConsole")
        return sb.toString()
    }

    /**
     * Loads the GameUserSettings.ini file from the server's installation location.
     * */
    fun loadGameUserSettings(): Result<GameUserSettings?> {
        val existingRawFile = Path.of(installationLocation, Constants.GAME_USER_SETTINGS_PATH)

        return if (existingRawFile.toFile().exists()) {
            try {
                Result.success(IniSerializer.deserialize<GameUserSettings>(existingRawFile.toFile().readText()))
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.success(null)
        }
    }

    /**
     * Saves the GameUserSettings.ini file to the server's installation location.
     * */
    fun saveGameUserSettings(): Result<Unit> {
        val iniFile = IniSerializer.serialize(gameUserSettings)
        val filePath = Path.of(installationLocation, Constants.GAME_USER_SETTINGS_PATH)
        if (!filePath.parent.toFile().exists()) {
            filePath.parent.toFile().mkdirs()
        }
        return try {
            filePath.toFile().writeText(iniFile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
