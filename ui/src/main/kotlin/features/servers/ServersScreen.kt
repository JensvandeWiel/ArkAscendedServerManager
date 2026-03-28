package eu.wynq.arkascendedservermanager.ui.features.servers

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.action_add_server
import arkascendedservermanager.ui.generated.resources.page_servers
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.IconActionButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.painter.hints.Size
import org.jetbrains.jewel.ui.theme.dividerStyle
import org.jetbrains.jewel.ui.typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServersScreen(component: ServersComponent) {
    val pageServers = stringResource(Res.string.page_servers)
    val actionAddServer = stringResource(Res.string.action_add_server)

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp),) {
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
        Divider(Orientation.Horizontal, Modifier
            .fillMaxWidth())
    }
}