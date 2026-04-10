@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.models

import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.core.support.Constants
import eu.wynq.arkascendedservermanager.core.support.isValidPath
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.dao.UuidEntity
import org.jetbrains.exposed.v1.dao.UuidEntityClass
import org.jetbrains.exposed.v1.json.json
import serialization.IniSerializer
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

val format = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}

object ServersTable : UuidTable("servers") {
    val profile_name = varchar("profile_name", 255)
    val installation_location = varchar("installation_location", 255)
    val asa_api = bool("asa_api").default(false)
    val settings = json<Settings>("settings",format)
    val game_user_settings = json<GameUserSettings>("game_user_settings",format)
}

class ServerEntity(id: EntityID<Uuid>) : UuidEntity(id) {
    companion object : UuidEntityClass<ServerEntity>(ServersTable)

    var profile_name by ServersTable.profile_name
    var installation_location by ServersTable.installation_location
    var asa_api by ServersTable.asa_api
    var settings by ServersTable.settings
    var game_user_settings by ServersTable.game_user_settings
}

data class Server(
    val id: Uuid,
    val profileName: String,
    val installationLocation: String,
    val asaApi: Boolean = false,
    val settings: Settings,
    val gameUserSettings: GameUserSettings
) {
    companion object {
        fun fromEntity(entity: ServerEntity) = Server(
            id = entity.id.value,
            profileName = entity.profile_name,
            installationLocation = entity.installation_location,
            settings = entity.settings,
            asaApi = entity.asa_api,
            gameUserSettings = entity.game_user_settings
        )

        fun applyToEntity(server: Server, entity: ServerEntity) {
            entity.profile_name = server.profileName
            entity.settings = server.settings
            entity.installation_location = server.installationLocation
            entity.asa_api = server.asaApi
            entity.game_user_settings = server.gameUserSettings
        }
    }

    fun validate() = validateProfileName() && settings.validate() && validateInstallationLocation() && gameUserSettings.validate()

    fun validateProfileName() = profileName.isNotBlank()
    fun validateInstallationLocation() = isValidPath(installationLocation)

    fun makeStartupScriptString(): String {
        val administration = settings.administration
        val executableName = if (asaApi) Constants.OVERSEER_EXECUTABLE_NAME else Constants.SERVER_EXECUTABLE_NAME
        val sb = StringBuilder()
        sb.append("start ")
        sb.append(Path.of(installationLocation, Constants.SERVER_BINARY_PATH, executableName).toAbsolutePath().toString())
        sb.append(" ")
        sb.append(administration.map)
        sb.append("?Port=${administration.serverPort}")
        sb.append("?QueryPort=${administration.queryPort}")
        if (administration.rconEnabled) {
            sb.append("?RCONEnabled=True")
            sb.append("?RCONPort=${administration.rconPort}")
        }
        if (!administration.serverPassword.isNullOrBlank()) {
            sb.append("?ServerPassword=${administration.serverPassword}")
        }
        sb.append("?ServerAdminPassword=${administration.adminPassword}")
        sb.append(" -WinLiveMaxPlayers=" + administration.slots)
        if (administration.mods.isNotEmpty()) {
            sb.append(" -mods=" + administration.mods.joinToString(","))
        }
        sb.append(" -OldConsole")
        sb.append(" -NoGameAnalytics")
        settings.options.getEnabledOptions().forEach { option ->
            sb.append(" $option")
        }
        return sb.toString()
    }

    fun getGusFromInstall(): Result<GameUserSettings?> = runCatching {
        val path = Path.of(installationLocation, Constants.GAME_USER_SETTINGS_PATH)
        if (path.toFile().exists()) {
            IniSerializer.deserialize<GameUserSettings>(path.toFile().readText())
        } else {
            null
        }
    }

    fun saveGusToInstall(): Result<Unit> = runCatching {
        val iniContent = IniSerializer.serialize(gameUserSettings)
        val path = Path.of(installationLocation, Constants.GAME_USER_SETTINGS_PATH)
        if (!path.parent.toFile().exists()) {
            path.parent.toFile().mkdirs()
        }
        path.toFile().writeText(iniContent)
    }
}