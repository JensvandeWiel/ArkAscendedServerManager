package eu.wynq.arkascendedservermanager.ui.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.action_save_settings
import arkascendedservermanager.ui.generated.resources.page_settings
import arkascendedservermanager.ui.generated.resources.settings_data_path
import arkascendedservermanager.ui.generated.resources.settings_data_path_hint
import arkascendedservermanager.ui.generated.resources.settings_steamcmd_path
import arkascendedservermanager.ui.generated.resources.settings_steamcmd_path_hint
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun SettingsScreen(component: SettingsComponent) {
    val model by component.model.subscribeAsState()

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp)) {
            Text(stringResource(Res.string.page_settings), style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = { component.saveSettings() }, enabled = model.isDirty()) {
                Text(stringResource(Res.string.action_save_settings))
            }
        }
        Divider(
            Orientation.Horizontal, Modifier
                .fillMaxWidth()
        )
        Column(Modifier.padding(6.dp, 10.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FormField(
                value = model.dataPath,
                onValueChange = component::onDataPathChanged,
                label = stringResource(Res.string.settings_data_path),
                hint = stringResource(Res.string.settings_data_path_hint),
                error = model.dataPathError,
            )
            FormField(
                value = model.steamCmdPath,
                onValueChange = component::onSteamCmdPathChanged,
                label = stringResource(Res.string.settings_steamcmd_path),
                hint = stringResource(Res.string.settings_steamcmd_path_hint),
                error = model.dataPathError,
            )
        }
    }
}