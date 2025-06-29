package ui.server.components.administration

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.ListItemSeparator
import io.github.composefluent.component.SecureTextField
import io.github.composefluent.component.Switcher
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.GeneralConfigurationModel
import ui.server.GeneralConfigurationComponent

@Composable
fun GeneralConfigurationRconSection(component: GeneralConfigurationComponent, generalConfigurationModel: GeneralConfigurationModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Switcher(
            checked = generalConfigurationModel.rconEnabled,
            onCheckStateChange = { newValue ->
                component.updateAdministrationModel(
                    component.generalConfigurationModel.value.copy(rconEnabled = newValue)
                )
            }
        )
        Text("RCON", style = FluentTheme.typography.bodyStrong)
    }
    ListItemSeparator(Modifier)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                enabled = generalConfigurationModel.rconEnabled,
                value = generalConfigurationModel.rconPort,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.generalConfigurationModel.value.copy(rconPort = newValue)
                    )
                },
                header = { Text("RCON Port") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            SecureTextField(
                enabled = generalConfigurationModel.rconEnabled,
                state = generalConfigurationModel.rconPassword,
                header = { Text("RCON Password") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
