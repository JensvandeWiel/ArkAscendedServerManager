package ui.server.components.administration

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.GeneralConfigurationModel
import ui.server.GeneralConfigurationComponent

@Composable
fun GeneralConfigurationPortsSection(component: GeneralConfigurationComponent, generalConfigurationModel: GeneralConfigurationModel) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                value = generalConfigurationModel.serverPort,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.generalConfigurationModel.value.copy(serverPort = newValue)
                    )
                },
                header = { Text("Server Port") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            TextField(
                value = generalConfigurationModel.queryPort,
                onValueChange = { newValue ->
                    component.updateAdministrationModel(
                        component.generalConfigurationModel.value.copy(queryPort = newValue)
                    )
                },
                header = { Text("Query Port") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
