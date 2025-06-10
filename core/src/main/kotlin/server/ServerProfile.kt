package server

import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class ServerProfile @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    val profileName: String,
    val installationLocation: String,
    val administrationConfig: AdministrationConfig = AdministrationConfig(serverName = profileName),
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
        sb.append(" -mods=" + administrationConfig.mods.joinToString(","))
        // sb.append(" -OldConsole")
        return sb.toString()
    }
}
