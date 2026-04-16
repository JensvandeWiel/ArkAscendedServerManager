@file:OptIn(ExperimentalFoundationApi::class, ExperimentalUuidApi::class)
package eu.wynq.arkascendedservermanager.ui.features.clusters

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import db.models.Cluster
import eu.wynq.arkascendedservermanager.ui.components.ConfirmationDialog
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.helpers.AppChangelog
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.markdown.standalone.ProvideMarkdownStyling
import org.jetbrains.jewel.markdown.Markdown
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.ExternalLink
import org.jetbrains.jewel.ui.component.IconActionButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ClustersScreen(component: ClustersComponent) {
    val clusters by component.clusters.collectAsState()
    val model by component.model.subscribeAsState()

    if (model.isDeleteClusterDialogOpen != null) {
        ConfirmationDialog(component::onDeletionDialogConfirmed, component::onDeletionDialogDismissed,
            title = stringResource(Res.string.clusters_delete_dialog_title),
            message = stringResource(Res.string.clusters_delete_dialog_message, model.isDeleteClusterDialogOpen?.name ?: ""),
        )
    }

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp)) {
            Text(stringResource(Res.string.clusters_title), style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            IconActionButton(
                key = AllIconsKeys.General.Add,
                contentDescription = stringResource(Res.string.clusters_add_cluster),
                onClick = component::createCluster,
                focusable = false,
                tooltip = { Text(stringResource(Res.string.clusters_add_cluster)) },
                tooltipPlacement = TooltipPlacement.ComponentRect(
                    Alignment.CenterStart,
                    Alignment.CenterStart,
                    offset = DpOffset((-6).dp, 0.dp)
                ),
            )
        }
        Divider(
            Orientation.Horizontal, Modifier
                .fillMaxWidth()
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            items(clusters.size) { index ->
                ClusterCard(
                    cluster = clusters.values.elementAt(index),
                    onClusterUpdate = component::updateCluster,
                    onClusterDeleteClicked = component::onDeleteClusterClicked,
                )
            }
        }
    }
}

@OptIn(ExperimentalJewelApi::class)
@Composable
fun ClusterCard(cluster: Cluster, onClusterUpdate: (Cluster) -> Unit, onClusterDeleteClicked: (Cluster) -> Unit) {
    Column(Modifier.fillMaxWidth().background(JewelTheme.globalColors.panelBackground, RoundedCornerShape(8.dp)).padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(cluster.name, style = JewelTheme.typography.h3TextStyle)
                    InfoText(cluster.id.toString())
                }
                Spacer(Modifier.weight(1f))
                IconActionButton(
                    key = AllIconsKeys.General.Delete,
                    contentDescription = stringResource(Res.string.clusters_remove_cluster),
                    onClick = { onClusterDeleteClicked(cluster)},
                    focusable = false,
                    tooltip = { Text(stringResource(Res.string.clusters_remove_cluster)) },
                    tooltipPlacement = TooltipPlacement.ComponentRect(
                        Alignment.CenterStart,
                        Alignment.CenterStart,
                        offset = DpOffset((-6).dp, 0.dp)
                    ),
                    enabled = cluster.servers.isEmpty()
                )
            }
        }
        Spacer(Modifier.weight(1f))
        FormTextField(
            value = cluster.name,
            onValueChange = { onClusterUpdate(cluster.copy(name = it)) },
            label = stringResource(Res.string.clusters_name_label),
            hint = stringResource(Res.string.clusters_name_hint),
        )
    }
}