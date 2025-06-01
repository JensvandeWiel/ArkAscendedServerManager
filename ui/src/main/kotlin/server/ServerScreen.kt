package ui.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.konyaco.fluent.component.AccentButton
import com.konyaco.fluent.component.ListItemSeparator
import com.konyaco.fluent.component.Text
import com.konyaco.fluent.component.TextField
import kotlinx.coroutines.launch

@Composable
fun ServerScreen(component: ServerComponent) {

    val profileConfigurationModel by component.profileConfigurationModel.subscribeAsState()
    val server by component.server.subscribeAsState()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(server.profileName, style = com.konyaco.fluent.FluentTheme.typography.title)
            Spacer(modifier = Modifier.weight(1f))
            AccentButton({
                component.saveProfileConfiguration()
            }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Save")
            }
        }
        ListItemSeparator(Modifier)
        Text("Profile Configuration", style = com.konyaco.fluent.FluentTheme.typography.subtitle)
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
