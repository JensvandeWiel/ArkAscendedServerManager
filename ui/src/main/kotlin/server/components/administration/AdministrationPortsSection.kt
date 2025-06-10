package ui.server.components.administration

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.konyaco.fluent.component.Text
import com.konyaco.fluent.component.TextField
import ui.server.AdministrationModel
import ui.server.ServerComponent

@Composable
fun AdministrationPortsSection(component: ServerComponent, administrationModel: AdministrationModel) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                value = administrationModel.serverPort,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.administrationModel.value.copy(serverPort = newValue)
                    )
                },
                header = { Text("Server Port") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                value = administrationModel.queryPort,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.administrationModel.value.copy(queryPort = newValue)
                    )
                },
                header = { Text("Query Port") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
