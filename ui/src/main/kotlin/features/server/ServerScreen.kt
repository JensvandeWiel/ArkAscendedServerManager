@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import arkascendedservermanager.ui.generated.resources.server_details_general_tab
import arkascendedservermanager.ui.generated.resources.server_details_server_name
import arkascendedservermanager.ui.generated.resources.server_details_title
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.DefaultSplitButton
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.SelectableIconActionButton
import org.jetbrains.jewel.ui.component.SimpleTabContent
import org.jetbrains.jewel.ui.component.TabData
import org.jetbrains.jewel.ui.component.TabStrip
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.theme.defaultTabStyle
import org.jetbrains.jewel.ui.typography
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ServerScreen(component: ServerComponent) {
    val title = stringResource(Res.string.server_details_title)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name)
    val notFoundLabel = stringResource(Res.string.server_details_not_found)
    val errorLabel = stringResource(Res.string.server_details_error)
    val infoTabLabel = stringResource(Res.string.server_details_info_tab)
    val generalTabLabel = stringResource(Res.string.server_details_general_tab)
    val model by component.model.subscribeAsState()
    val error by component.error.collectAsState()
    val selectedTab by component.selectedTab.subscribeAsState()
    val tabs =
        remember(selectedTab, infoTabLabel, generalTabLabel) {
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
                    selected = selectedTab == ServerDetailsTab.GENERAL,
                    content = { tabState ->
                        SimpleTabContent(label = generalTabLabel, state = tabState)
                    },
                    onClick = component::selectGeneralTab,
                    closable = false,
                ),
            )
        }

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = JewelTheme.typography.h2TextStyle)
            Text("/", style = JewelTheme.typography.h2TextStyle)
            Text(model.initialServer?.profileName ?: "", style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::saveServer, enabled = model.isDirty()) {
                Text("Save server")
            }
        }
        TabStrip(
            tabs = tabs,
            style = JewelTheme.defaultTabStyle,
            modifier = Modifier.fillMaxWidth(),
        )
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            val loadedServer = model.initialServer
            if (loadedServer != null) {
                when (selectedTab) {
                    ServerDetailsTab.INFO -> {
                        InfoTabContent(component)
                    }
                    ServerDetailsTab.GENERAL -> {
                        GeneralTabContent(component)
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

@Composable
fun InfoTabContent(component: ServerComponent) {
    val serverNameLabel = stringResource(Res.string.server_details_server_name)
    val installationLocationLabel = stringResource(Res.string.server_details_installation_location)
    val model by component.model.subscribeAsState()
    Text("Info tab: ${model.server?.serverName}")
}

@Composable
fun GeneralTabContent(component: ServerComponent) {
    val model by component.model.subscribeAsState()
    GroupHeader("Profile")
    FormField(
        value = model.server?.profileName ?: "",
        onValueChange = { newValue ->
            component.updateServer { it ->
                it.copy(profileName = newValue)
            }
        },
        label = "Profile name:",
        error = model.server?.validateProfileName() == false,
        labelPosition = LabelPosition.Above,
    )
    FormField(
        value = model.server?.installationLocation ?: "",
        onValueChange = { newValue ->
            component.updateServer { it ->
                it.copy(installationLocation = newValue)
            }
        },
        label = "Installation path:",
        error = model.server?.validateInstallationLocation() == false,
        hint = "Path to the server installation folder, changing this will not move the existing installation.",
        labelPosition = LabelPosition.Above,
    )
    GroupHeader("Server")
    FormField(
        value = model.server?.serverName ?: "",
        onValueChange = { newValue ->
            component.updateServer { it ->
                it.copy(serverName = newValue)
            }
        },
        label = "Server name:",
        error = model.server?.validateServerName() == false,
        labelPosition = LabelPosition.Above,
        hint = "Public name of the server",
    )
}
