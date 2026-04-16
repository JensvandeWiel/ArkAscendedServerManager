@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.stores

import db.models.Cluster
import db.repositories.ClustersRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ClustersStore {
    val clusters: StateFlow<Map<Uuid, Cluster>>
    val error: StateFlow<String?>
    fun loadClusters()
    fun reloadClusters()
    fun createCluster(): Result<Cluster>
    fun getCluster(clusterId: Uuid): Result<Cluster>
    fun deleteCluster(cluster: Cluster): Result<Unit>
    fun updateCluster(cluster: Cluster)
}

class ClustersStoreImpl : ClustersStore {
    private val logger = KotlinLogging.logger {}
    private val _clusters = MutableStateFlow<Map<Uuid, Cluster>>(emptyMap())
    override val clusters: StateFlow<Map<Uuid, Cluster>> = _clusters.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    override val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadClusters()
    }

    override fun loadClusters() {
        val dbClusters = ClustersRepository.getClusters()
        logger.debug { "Loaded ${dbClusters.getOrNull()?.size} clusters: ${dbClusters.getOrNull()?.joinToString { it.name + "(${it.servers.size})" }}" }
        if (dbClusters.isSuccess) {
            _clusters.value = dbClusters.getOrThrow().associateBy { it.id }
            _error.value = null
        } else {
            logger.error(dbClusters.exceptionOrNull()) { "Failed to load clusters" }
            _error.value = dbClusters.exceptionOrNull()?.message
        }
    }

    override fun reloadClusters() {
        loadClusters()
    }

    override fun createCluster(): Result<Cluster> {
        val newCluster = ClustersRepository.createCluster()
        if (newCluster.isSuccess) {
            val cluster = newCluster.getOrThrow()
            _clusters.value = _clusters.value + (cluster.id to cluster)
            _error.value = null
            return Result.success(cluster)
        } else {
            logger.error(newCluster.exceptionOrNull()) { "Failed to create cluster: ${newCluster.exceptionOrNull()}" }
            _error.value = newCluster.exceptionOrNull()?.message
            return Result.failure(newCluster.exceptionOrNull() ?: IllegalStateException("Unknown error"))
        }
    }

    override fun getCluster(clusterId: Uuid): Result<Cluster> {
        clusters.value[clusterId]?.let { return Result.success(it) }
        logger.warn { "Cluster not found: $clusterId" }
        return Result.failure(IllegalStateException("Cluster not found"))
    }

    override fun deleteCluster(cluster: Cluster): Result<Unit> {
        val res = ClustersRepository.deleteCluster(cluster)
        if (res.isSuccess) {
            _clusters.value = _clusters.value - cluster.id
            _error.value = null
            return Result.success(Unit)
        } else {
            logger.error(res.exceptionOrNull()) { "Failed to delete cluster ${cluster.name} (${cluster.id})" }
            _error.value = res.exceptionOrNull()?.message
            return Result.failure(res.exceptionOrNull() ?: IllegalStateException("Unknown error"))
        }
    }

    override fun updateCluster(cluster: Cluster) {
        ClustersRepository.saveCluster(cluster).onSuccess {
            _clusters.value = _clusters.value + (cluster.id to cluster)
            _error.value = null
        }.onFailure {
            logger.error(it) { "Failed to save cluster ${cluster.name} (${cluster.id})" }
            _error.value = it.message
        }
    }
}

