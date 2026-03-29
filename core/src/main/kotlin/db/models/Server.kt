@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db.models

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
}

class ServerEntity(id: EntityID<Uuid>) : UuidEntity(id) {
    companion object : UuidEntityClass<ServerEntity>(ServersTable)

    var profile_name by ServersTable.profile_name
    var server_name by ServersTable.server_name
    var installation_location by ServersTable.installation_location
}

class Server(
    val id: Uuid,
    val profileName: String,
    val serverName: String,
    val installationLocation: String,
) {
    companion object {
        fun fromEntity(entity: ServerEntity) = Server(
            id = entity.id.value,
            profileName = entity.profile_name,
            serverName = entity.server_name,
            installationLocation = entity.installation_location,
        )
    }
}