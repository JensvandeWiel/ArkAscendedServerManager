@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.clusters

import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import db.models.Cluster
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import eu.wynq.arkascendedservermanager.ui.stores.ClustersStore
import features.clusters.ClustersModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi

class ClustersComponent(componentContext: ComponentContext) : ComponentContext by componentContext, KoinComponent {
    val clustersStore: ClustersStore by inject()
    private val _model: MutableValue<ClustersModel> = MutableValue(ClustersModel())
    val model: Value<ClustersModel> get() = _model


    val clusters = clustersStore.clusters

    init {
        clustersStore.loadClusters()
    }

    fun updateCluster(cluster: Cluster): Unit = clustersStore.updateCluster(cluster)

    fun createCluster() {
        val res = clustersStore.createCluster()
        if (res.isFailure) {
            runBlocking {
                val error = res.exceptionOrNull()?.message ?: res.exceptionOrNull()?.javaClass?.simpleName ?: getString(
                    Res.string.error_unknown
                )
                ToastBannerManager.show(
                    ToastBannerType.ERROR,
                    getString(Res.string.clusters_error_create_failed, error)
                )
            }
        }
    }

    private fun deleteCluster(cluster: Cluster) {
        val res = clustersStore.deleteCluster(cluster)
        if (res.isFailure) {
            runBlocking {
                if (res.exceptionOrNull() is IllegalStateException && res.exceptionOrNull()?.message == "Cannot delete cluster with associated servers") {
                    ToastBannerManager.show(
                        ToastBannerType.ERROR,
                        getString(Res.string.clusters_error_delete_has_servers)
                    )
                } else {
                    val error = res.exceptionOrNull()?.message ?: res.exceptionOrNull()?.javaClass?.simpleName
                    ?: getString(Res.string.error_unknown)
                    ToastBannerManager.show(
                        ToastBannerType.ERROR,
                        getString(Res.string.clusters_error_delete_failed, error)
                    )
                }
            }
        }
    }

    fun onDeleteClusterClicked(cluster: Cluster) {
        _model.update {
            it.copy(
                isDeleteClusterDialogOpen = cluster,
            )
        }
    }

    fun onDeletionDialogConfirmed() {
        deleteCluster(model.value.isDeleteClusterDialogOpen!!)
        _model.update {
            it.copy(
                isDeleteClusterDialogOpen = null,
            )
        }
    }

    fun onDeletionDialogDismissed() {
        _model.update {
            it.copy(
                isDeleteClusterDialogOpen = null,
            )
        }
    }
}