package ui.server.components.administration

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.CheckBox
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.AdministrationModel
import ui.server.ServerComponent

@Composable
fun AdministrationBasicFields(component: ServerComponent, administrationModel: AdministrationModel) {
    TextField(
        value = administrationModel.serverName,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.administrationModel.value.copy(serverName = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Server Name") },
    )
    TextField(
        value = administrationModel.serverPassword,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.administrationModel.value.copy(serverPassword = newValue)
            )
        },
        enabled = administrationModel.serverPasswordEnabled,
        modifier = Modifier.fillMaxWidth(),
        header = {
            CheckBox(
                administrationModel.serverPasswordEnabled,
                "Server Password",
                onCheckStateChange = {
                    component.updateAdministrationModel(
                        administrationModel.copy(
                            serverPasswordEnabled = it,
                            serverPassword = TextFieldValue("")
                        )
                    )
                }
            )
        },
    )
    TextField(
        value = administrationModel.adminPassword,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.administrationModel.value.copy(adminPassword = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Admin Password") },
    )
    TextField(
        value = administrationModel.map,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.administrationModel.value.copy(map = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Map") },
    )
    TextField(
        value = administrationModel.slots,
        onValueChange = { newValue ->
            component.updateAdministrationModel(
                component.administrationModel.value.copy(slots = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = { Text("Slots") },
    )
}
