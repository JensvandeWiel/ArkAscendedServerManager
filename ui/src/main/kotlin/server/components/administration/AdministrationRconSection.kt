package ui.server.components.administration

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.ListItemSeparator
import io.github.composefluent.component.Switcher
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.AdministrationModel
import ui.server.ServerComponent

@Composable
fun AdministrationRconSection(component: ServerComponent, administrationModel: AdministrationModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Switcher(
            checked = administrationModel.rconEnabled,
            onCheckStateChange = { newValue ->
                component.updateAdministrationModel(
                    component.administrationModel.value.copy(rconEnabled = newValue)
                )
            }
        )
        Text("RCON", style = FluentTheme.typography.bodyStrong)
    }
    ListItemSeparator(Modifier)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                enabled = administrationModel.rconEnabled,
                value = administrationModel.rconPort,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.administrationModel.value.copy(rconPort = newValue)
                    )
                },
                header = { Text("RCON Port") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                enabled = administrationModel.rconEnabled,
                value = administrationModel.rconPassword,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.administrationModel.value.copy(rconPassword = newValue)
                    )
                },
                header = { Text("RCON Password") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
