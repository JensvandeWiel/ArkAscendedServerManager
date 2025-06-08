package ui.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.konyaco.fluent.component.AccentButton
import com.konyaco.fluent.component.ListItemSeparator
import com.konyaco.fluent.component.ProgressBar
import com.konyaco.fluent.component.Text
import com.konyaco.fluent.component.TextField
import components.CollapsibleCard

@Composable
fun ServerScreen(component: ServerComponent) {

    val profileConfigurationModel by component.profileConfigurationModel.subscribeAsState()
    val server by component.server.subscribeAsState()
    val installationModel by component.installationModel.subscribeAsState()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp, 0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Profile: " + server.profileName, style = com.konyaco.fluent.FluentTheme.typography.body)
                Spacer(modifier = Modifier.weight(1f))
                AccentButton({
                    component.saveProfileConfiguration()
                }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Save")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (installationModel.isInstalling) {
                    Text(
                        "Installation status: " + installationModel.message.orEmpty(),
                        style = com.konyaco.fluent.FluentTheme.typography.body
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (installationModel.progress != null) {
                        ProgressBar(
                            progress = installationModel.progress!! / 100f,
                            modifier = Modifier.width(128.dp).padding(start = 8.dp)
                        )
                    } else {
                        ProgressBar(Modifier.width(128.dp))
                    }
                } else {
                    Text("Installation status: " + if (server.getServerManager().getInstallManager().isInstalled()) "Installed" else "Not Installed")
                    Spacer(modifier = Modifier.weight(1f))
                    AccentButton(
                        onClick = {
                            component.startServerInstallation()
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(if (server.getServerManager().getInstallManager().isInstalled()) "Update" else "Install")
                    }
                }
            }
        }
        ListItemSeparator(Modifier)
        CollapsibleCard(title = { Text("Profile") }) {
            TextField(
                value = profileConfigurationModel.profileName,
                onValueChange = { newValue ->
                    component.updateProfileConfigurationModel(
                        component.profileConfigurationModel.value.copy(profileName = newValue)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                header = { Text("Profile Name") },
            )
            TextField(
                value = profileConfigurationModel.installationLocation,
                onValueChange = { newValue ->
                    component.updateProfileConfigurationModel(
                        component.profileConfigurationModel.value.copy(installationLocation = newValue)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                header = { Text("Installation Location") },
            )
        }
    }
}
