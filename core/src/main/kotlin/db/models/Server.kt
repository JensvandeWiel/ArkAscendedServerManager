@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.models

import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.core.support.isValidPath
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.dao.UuidEntity
import org.jetbrains.exposed.v1.dao.UuidEntityClass
import org.jetbrains.exposed.v1.json.json
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
}

class ServerEntity(id: EntityID<Uuid>) : UuidEntity(id) {
    companion object : UuidEntityClass<ServerEntity>(ServersTable)

    var profile_name by ServersTable.profile_name
    var installation_location by ServersTable.installation_location
    var asa_api by ServersTable.asa_api
    var settings by ServersTable.settings
}

data class Server(
    val id: Uuid,
    val profileName: String,
    val installationLocation: String,
    val asaApi: Boolean = false,
    val settings: Settings
) {
    companion object {
        fun fromEntity(entity: ServerEntity) = Server(
            id = entity.id.value,
            profileName = entity.profile_name,
            installationLocation = entity.installation_location,
            settings = entity.settings,
            asaApi = entity.asa_api
        )

        fun applyToEntity(server: Server, entity: ServerEntity) {
            entity.profile_name = server.profileName
            entity.settings = server.settings
            entity.installation_location = server.installationLocation
            entity.asa_api = server.asaApi
        }
    }

    fun validate() = validateProfileName() && settings.validate() && validateInstallationLocation()

    fun validateProfileName() = profileName.isNotBlank()
    fun validateInstallationLocation() = isValidPath(installationLocation)
}