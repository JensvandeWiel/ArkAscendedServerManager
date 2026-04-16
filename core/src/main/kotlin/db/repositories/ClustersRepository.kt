@file:OptIn(ExperimentalUuidApi::class)

package db.repositories

import com.oblac.nomen.Nomen
import db.models.Cluster
import db.models.ClusterEntity
import eu.wynq.arkascendedservermanager.core.db.models.ServerEntity
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ClustersRepository {
    fun createCluster(): Result<Cluster> = runCatching {
        val name = Nomen.est().adjective().color().get()

        transaction {
            Cluster.fromEntity(ClusterEntity.new {
                this.name = name
            })
        }

    }

    fun getCluster(clusterId: Uuid): Result<Cluster> = runCatching {
        Cluster.fromEntity(transaction {
            ClusterEntity.findById(clusterId)!!.load(ServerEntity::cluster)
        })
    }

    fun getClusters(): Result<List<Cluster>> = runCatching {
        transaction {
            ClusterEntity.all().with(ServerEntity::cluster).map(Cluster::fromEntity)
        }
    }

    fun deleteCluster(cluster: Cluster) = runCatching {
        if (cluster.servers.isNotEmpty()) {
            throw IllegalStateException("Cannot delete cluster with associated servers")
        }

        transaction {
            ClusterEntity.findById(cluster.id)?.delete()
        }
    }

    fun saveCluster(cluster: Cluster) = runCatching {
        transaction {
            ClusterEntity.findById(cluster.id)?.let { entity ->
                Cluster.applyToEntity(cluster, entity)
            } ?: throw IllegalStateException("Cluster not found")
        }
    }
}