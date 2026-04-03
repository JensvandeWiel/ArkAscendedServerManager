@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.servers

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.action_add_server
import arkascendedservermanager.ui.generated.resources.page_servers
import arkascendedservermanager.ui.generated.resources.page_servers_empty_state
import arkascendedservermanager.ui.generated.resources.server_details_go_to_server
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.ui.helpers.PreviewWrapper
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServersScreen(component: ServersComponent) {
    val pageServers = stringResource(Res.string.page_servers)
    val pageServersEmptyState = stringResource(Res.string.page_servers_empty_state)
    val actionAddServer = stringResource(Res.string.action_add_server)
    val servers = component.servers.collectAsState()

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp)) {
            Text(pageServers, style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            IconActionButton(
                key = AllIconsKeys.General.Add,
                contentDescription = actionAddServer,
                onClick = component::onServerAddClicked,
                focusable = false,
                tooltip = { Text(actionAddServer) },
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
        VerticallyScrollableContainer(Modifier.fillMaxSize()) {
            if (servers.value.values.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(pageServersEmptyState)
                }
            } else {
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    servers.value.values.forEach { server ->
                        ServerCard(
                            server = server,
                            onClick = { component.onServerClicked(server) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServerCard(server: Server, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor = if (isHovered) {
        JewelTheme.globalColors.panelBackground.copy(alpha = 0.82f)
    } else {
        JewelTheme.globalColors.panelBackground
    }
    val goToServerDescription = stringResource(Res.string.server_details_go_to_server)

    Row(
        Modifier
            .fillMaxWidth()
            .pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(94.dp),
    ) {
        Text(server.profileName, style = JewelTheme.typography.h2TextStyle)
        Spacer(Modifier.weight(1f))
        Icon(
            AllIconsKeys.General.ArrowRight,
            contentDescription = goToServerDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
@Preview
private fun ServerCardPreview() {
    val server = Server(
        Uuid.random(), "Profile Name", "Server Name", "Installation Location"
    )
    PreviewWrapper {
        ServerCard(server = server, onClick = {})
    }
}