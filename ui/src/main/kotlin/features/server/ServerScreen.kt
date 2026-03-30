package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_details_error
import arkascendedservermanager.ui.generated.resources.server_details_info_tab
import arkascendedservermanager.ui.generated.resources.server_details_installation_location
import arkascendedservermanager.ui.generated.resources.server_details_not_found
import arkascendedservermanager.ui.generated.resources.server_details_profile_name
import arkascendedservermanager.ui.generated.resources.server_details_profile_tab
import arkascendedservermanager.ui.generated.resources.server_details_server_name
import arkascendedservermanager.ui.generated.resources.server_details_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.SimpleTabContent
import org.jetbrains.jewel.ui.component.TabData
import org.jetbrains.jewel.ui.component.TabStrip
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.theme.defaultTabStyle
import org.jetbrains.jewel.ui.theme.editorTabStyle
import org.jetbrains.jewel.ui.typography

@Composable
fun ServerScreen(component: ServerComponent) {
    val title = stringResource(Res.string.server_details_title)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name)
    val serverNameLabel = stringResource(Res.string.server_details_server_name)
    val installationLocationLabel = stringResource(Res.string.server_details_installation_location)
    val notFoundLabel = stringResource(Res.string.server_details_not_found)
    val errorLabel = stringResource(Res.string.server_details_error)
    val infoTabLabel = stringResource(Res.string.server_details_info_tab)
    val profileTabLabel = stringResource(Res.string.server_details_profile_tab)
    val server by component.server.collectAsState()
    val error by component.error.collectAsState()
    val selectedTab by component.selectedTab.collectAsState()
    val tabs =
        remember(selectedTab, infoTabLabel, profileTabLabel) {
            listOf(
                TabData.Default(
                    selected = selectedTab == ServerDetailsTab.INFO,
                    content = { tabState ->
                        SimpleTabContent(label = infoTabLabel, state = tabState)
                    },
                    onClick = component::selectInfoTab,
                    closable = false,
                ),
                TabData.Default(
                    selected = selectedTab == ServerDetailsTab.PROFILE,
                    content = { tabState ->
                        SimpleTabContent(label = profileTabLabel, state = tabState)
                    },
                    onClick = component::selectProfileTab,
                    closable = false,
                ),
            )
        }

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = JewelTheme.typography.h2TextStyle)
            Text("/", style = JewelTheme.typography.h2TextStyle)
            Text(server?.profileName ?: "", style = JewelTheme.typography.h2TextStyle)
        }
        TabStrip(
            tabs = tabs,
            style = JewelTheme.defaultTabStyle,
            modifier = Modifier.fillMaxWidth(),
        )
        Divider(
            Orientation.Horizontal, Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            val loadedServer = server
            if (loadedServer != null) {
                when (selectedTab) {
                    ServerDetailsTab.INFO -> {
                        Text("$serverNameLabel: ${loadedServer.serverName}")
                        Text("$installationLocationLabel: ${loadedServer.installationLocation}")
                    }

                    ServerDetailsTab.PROFILE -> {
                        Text("$profileNameLabel: ${loadedServer.profileName}")
                    }
                }
            } else if (error != null) {
                Text("$errorLabel: $error")
            } else {
                Text(notFoundLabel)
            }
        }
    }
}

