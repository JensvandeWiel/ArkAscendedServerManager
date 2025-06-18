package ui.server.components.administration

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.GameUserSettingsModel
import ui.server.ServerComponent

@Composable
fun AdministrationAutoSaveSection(
    component: ServerComponent,
    gameUserSettingsModel: GameUserSettingsModel
) {
    Row {
        TextField(
            value = gameUserSettingsModel.autoSavePeriod,
            onValueChange = { newValue ->
                // Check if the new value is a valid number
                val newValueFloat = newValue.text.toFloatOrNull()
                if (newValueFloat == null || newValueFloat <= 0) {
                    // If not valid, reset to the default value (e.g., 15 minutes)
                    return@TextField
                }
                component.updateGameUserSettingsModel(
                    gameUserSettingsModel.copy(autoSavePeriod = newValue)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            header = {
                Text("Auto-Save Period (minutes)")
            }
        )
    }
}
