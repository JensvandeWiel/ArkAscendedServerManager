@file:OptIn(ExperimentalUuidApi::class)

package db.models

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.db.models.ServerEntity
import eu.wynq.arkascendedservermanager.core.db.models.ServersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.dao.UuidEntity
import org.jetbrains.exposed.v1.dao.UuidEntityClass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ClustersTable : UuidTable("clusters") {
    val name = varchar("name", 255)
}

class ClusterEntity(id: EntityID<Uuid>) : UuidEntity(id) {
    companion object : UuidEntityClass<ClusterEntity>(ClustersTable)

    var name by ClustersTable.name
    val servers by ServerEntity optionalReferrersOn ServersTable.cluster
}


data class Cluster(val id: Uuid, val name: String, val servers: List<Server> = emptyList()) {
    companion object {
        fun fromEntity(entity: ClusterEntity, includeServers: Boolean = true) = Cluster(
            id = entity.id.value,
            name = entity.name,
            servers = if (includeServers) entity.servers.map { Server.fromEntity(it) } else emptyList()
        )

        fun toEntity(cluster: Cluster) = ClusterEntity.new(cluster.id) {
            name = cluster.name
        }

        fun applyToEntity(cluster: Cluster, entity: ClusterEntity) {
            entity.name = cluster.name
        }
    }

    fun validate() = name.isNotBlank() && name.length <= 255
}