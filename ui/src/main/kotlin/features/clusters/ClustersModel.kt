@file:OptIn(ExperimentalUuidApi::class)

package features.clusters

import db.models.Cluster
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ClustersModel(
    val isDeleteClusterDialogOpen: Cluster? = null,
)