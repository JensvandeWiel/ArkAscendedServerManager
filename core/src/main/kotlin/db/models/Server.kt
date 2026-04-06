@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.models

import eu.wynq.arkascendedservermanager.core.support.isValidPath
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.dao.UuidEntity
import org.jetbrains.exposed.v1.dao.UuidEntityClass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ServersTable : UuidTable("servers") {
    val profile_name = varchar("profile_name", 255)
    val server_name = varchar("server_name", 255)
    val installation_location = varchar("installation_location", 255)
    val asa_api = bool("asa_api").default(false)
}

class ServerEntity(id: EntityID<Uuid>) : UuidEntity(id) {
    companion object : UuidEntityClass<ServerEntity>(ServersTable)

    var profile_name by ServersTable.profile_name
    var server_name by ServersTable.server_name
    var installation_location by ServersTable.installation_location
    var asa_api by ServersTable.asa_api
}

data class Server(
    val id: Uuid,
    val profileName: String,
    val serverName: String,
    val installationLocation: String,
    val asaApi: Boolean = false,
) {
    companion object {
        fun fromEntity(entity: ServerEntity) = Server(
            id = entity.id.value,
            profileName = entity.profile_name,
            serverName = entity.server_name,
            installationLocation = entity.installation_location,
            asaApi = entity.asa_api
        )

        fun applyToEntity(server: Server, entity: ServerEntity) {
            entity.profile_name = server.profileName
            entity.server_name = server.serverName
            entity.installation_location = server.installationLocation
            entity.asa_api = server.asaApi
        }
    }

    fun validate() = validateProfileName() && validateServerName() && validateInstallationLocation()

    fun validateProfileName() = profileName.isNotBlank()
    fun validateServerName() = serverName.isNotBlank()
    fun validateInstallationLocation() = isValidPath(installationLocation)
}