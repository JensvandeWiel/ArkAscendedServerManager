@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.dao.UuidEntity
import org.jetbrains.exposed.v1.dao.UuidEntityClass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Servers : UuidTable("servers") {
    val profile_name = varchar("profile_name", 255)
    val server_name = varchar("server_name", 255)
    val installation_location = varchar("installation_location", 255)
}

class ServerEntity(id: EntityID<Uuid>) : UuidEntity(id) {
    companion object : UuidEntityClass<ServerEntity>(Servers)

    var profile_name by Servers.profile_name
    var server_name by Servers.server_name
    var installation_location by Servers.installation_location
}