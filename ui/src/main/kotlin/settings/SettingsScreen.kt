package ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.component.AccentButton
import io.github.composefluent.component.ListItemSeparator
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField

@Composable
fun SettingsScreen(component: SettingsComponent) {
    val model by component.model.subscribeAsState()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                modifier = Modifier,
                style = io.github.composefluent.FluentTheme.typography.title
            )
            Spacer(modifier = Modifier.weight(1f))
            AccentButton(onClick = {
                component.saveSettings()
            }) {
                Text("Save Settings")
            }
        }
        ListItemSeparator(Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column {

            }
            TextField(
                value = model.steamCmdPath,
                header = {
                    Text("SteamCMD Path", style = io.github.composefluent.FluentTheme.typography.body)
                },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { component.updateSteamCmdPath(it) }
            )
            TextField(
                value = model.applicationDataPath,
                header = {
                    Text("Application Data Path", style = io.github.composefluent.FluentTheme.typography.body)
                },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { component.updateApplicationDataPath(it) }
            )
        }
    }
}
