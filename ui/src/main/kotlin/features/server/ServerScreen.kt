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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_details_error
import arkascendedservermanager.ui.generated.resources.server_details_installation_location
import arkascendedservermanager.ui.generated.resources.server_details_not_found
import arkascendedservermanager.ui.generated.resources.server_details_profile_name
import arkascendedservermanager.ui.generated.resources.server_details_server_name
import arkascendedservermanager.ui.generated.resources.server_details_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun ServerScreen(component: ServerComponent) {
    val title = stringResource(Res.string.server_details_title)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name)
    val serverNameLabel = stringResource(Res.string.server_details_server_name)
    val installationLocationLabel = stringResource(Res.string.server_details_installation_location)
    val notFoundLabel = stringResource(Res.string.server_details_not_found)
    val errorLabel = stringResource(Res.string.server_details_error)
    val server by component.server.collectAsState()
    val error by component.error.collectAsState()

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp)) {
            Text(title, style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
        }
        Divider(
            Orientation.Horizontal, Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (server != null) {
                Text("$profileNameLabel: ${server!!.profileName}")
                Text("$serverNameLabel: ${server!!.serverName}")
                Text("$installationLocationLabel: ${server!!.installationLocation}")
            } else if (error != null) {
                Text("$errorLabel: $error")
            } else {
                Text(notFoundLabel)
            }
        }
    }
}

