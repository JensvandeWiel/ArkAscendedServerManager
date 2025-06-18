package ui.server.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import components.CollapsibleCard
import ui.server.ServerComponent

@Composable
fun ProfileConfigurationSection(component: ServerComponent) {
    val profileConfigurationModel by component.profileConfigurationModel.subscribeAsState()

    CollapsibleCard(
        title = { Text("Profile", style = FluentTheme.typography.bodyStrong) },
        initiallyExpanded = false
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
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
