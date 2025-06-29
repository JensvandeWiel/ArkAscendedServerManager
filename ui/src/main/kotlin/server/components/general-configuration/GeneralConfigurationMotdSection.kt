package ui.server.components.administration

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composefluent.component.Text
import io.github.composefluent.component.TextField
import ui.server.GameUserSettingsModel
import ui.server.GeneralConfigurationComponent

@Composable
fun GeneralConfigurationMotdSection(
    component: GeneralConfigurationComponent,
    gameUserSettingsModel: GameUserSettingsModel
) {
    TextField(
        value = gameUserSettingsModel.motdDuration,
        onValueChange = { newValue ->
            // Check if the new value is a valid number
            val newValueFloat = newValue.text.toFloatOrNull()
            if (newValueFloat == null || newValueFloat <= 0) {
                // If not valid, reset to the default value (e.g., 15 minutes)
                return@TextField
            }
            component.updateGameUserSettingsModel(
                gameUserSettingsModel.copy(motdDuration = newValue)
            )
            println(component.gameUserSettingsModel.value)
        },
        modifier = Modifier.fillMaxWidth(),
        header = {
            Text("Message of the day duration (seconds)")
        }
    )
    TextField(
        value = gameUserSettingsModel.messageOfTheDay,
        onValueChange = { newValue ->
            component.updateGameUserSettingsModel(
                gameUserSettingsModel.copy(messageOfTheDay = newValue)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        header = {
            Text("Message of the day")
        }
    )
}
