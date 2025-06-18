package ui.server.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.AccentButton
import io.github.composefluent.component.ProgressBar
import io.github.composefluent.component.Text
import ui.server.ServerComponent

@Composable
fun InstallationSection(component: ServerComponent) {
    val server by component.server.subscribeAsState()
    val installationModel by component.installationModel.subscribeAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (installationModel.isInstalling) {
            Text(
                "Installation status: " + installationModel.message.orEmpty(),
                style = FluentTheme.typography.body
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
            Text(
                "Installation status: " + if (server.getServerManager().getInstallManager()
                        .isInstalled()
                ) "Installed" else "Not Installed"
            )
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
