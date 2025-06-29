package ui.server.components.administration

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import io.github.composefluent.component.CheckBox
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.GeneralConfigurationModel
import ui.server.GeneralConfigurationComponent

@Composable
fun GeneralConfigurationBasicFields(component: GeneralConfigurationComponent, generalConfigurationModel: GeneralConfigurationModel) {
    TextField(
        value = generalConfigurationModel.serverName,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.generalConfigurationModel.value.copy(serverName = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Server Name") },
    )
    TextField(
        value = generalConfigurationModel.serverPassword,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.generalConfigurationModel.value.copy(serverPassword = newValue)
            )
        },
        enabled = generalConfigurationModel.serverPasswordEnabled,
        modifier = Modifier.fillMaxWidth(),
        header = {
            CheckBox(
                generalConfigurationModel.serverPasswordEnabled,
                "Server Password",
                onCheckStateChange = {
                    component.updateAdministrationModel(
                        generalConfigurationModel.copy(
                            serverPasswordEnabled = it,
                            serverPassword = TextFieldValue("")
                        )
                    )
                }
            )
        },
    )
    TextField(
        value = generalConfigurationModel.adminPassword,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.generalConfigurationModel.value.copy(adminPassword = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Admin Password") },
    )
    TextField(
        value = generalConfigurationModel.map,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.generalConfigurationModel.value.copy(map = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Map") },
    )
    TextField(
        value = generalConfigurationModel.slots,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.generalConfigurationModel.value.copy(slots = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Slots") },
    )
}
