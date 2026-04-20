@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormSelectField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ClusterSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val clusterLabel = stringResource(Res.string.server_details_cluster_label)
    val clusterIdLabel = stringResource(Res.string.server_details_cluster_id_label)
    val clusterIdHint = stringResource(Res.string.server_details_cluster_id_hint)
    val clusterDirOverrideLabel = stringResource(Res.string.server_details_cluster_dir_override_label)
    val clusterDirOverrideHint = stringResource(Res.string.server_details_cluster_dir_override_hint)
    val clustersConfigurationGroup = stringResource(Res.string.clusters_configuration_group)
    val clusters by component.clusters.collectAsState()


    model.server?.run {
        GroupHeader(clustersConfigurationGroup)
        FormSelectField(
            value = cluster,
            onValueChange = component::updateServerCluster,
            options = clusters.values.toList(),
            optionLabel = { clusterOption ->
                clusterOption?.name ?: runBlocking { getString(Res.string.clusters_none) }
            },
            label = clusterLabel,
            compareWith = { a, b -> a?.id == b?.id }
        )
        FormTextField(
            value = settings.administration.clusterId ?: "",
            onValueChange = {},
            label = clusterIdLabel,
            readOnly = true,
            hint = clusterIdHint,
        )
        FormTextField(
            value = settings.administration.clusterDirOverride ?: "",
            onValueChange = {},
            label = clusterDirOverrideLabel,
            readOnly = true,
            hint = clusterDirOverrideHint,
        )
    }
}

