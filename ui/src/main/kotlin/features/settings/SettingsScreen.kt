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
import arkascendedservermanager.ui.generated.resources.settings_update_channel
import arkascendedservermanager.ui.generated.resources.settings_update_channel_hint
import arkascendedservermanager.ui.generated.resources.settings_update_channel_rc
import arkascendedservermanager.ui.generated.resources.settings_update_channel_stable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormPathField
import eu.wynq.arkascendedservermanager.ui.components.FormSelectField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.helpers.UpdateChannel
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
    val stableChannelLabel = stringResource(Res.string.settings_update_channel_stable)
    val rcChannelLabel = stringResource(Res.string.settings_update_channel_rc)

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
            FormTextField(
                value = model.dataPath,
                onValueChange = component::onDataPathChanged,
                label = stringResource(Res.string.settings_data_path),
                hint = stringResource(Res.string.settings_data_path_hint),
                error = model.dataPathError,
            )
            FormPathField(
                value = model.steamCmdPath,
                onValueChange = component::onSteamCmdPathChanged,
                label = stringResource(Res.string.settings_steamcmd_path),
                hint = stringResource(Res.string.settings_steamcmd_path_hint),
                error = model.dataPathError,
            )
            FormSelectField(
                value = model.updateChannel,
                onValueChange = { channel ->
                    if (channel != null) component.onUpdateChannelChanged(channel)
                },
                options = UpdateChannel.entries,
                optionLabel = { channel ->
                    when (channel ?: UpdateChannel.STABLE) {
                        UpdateChannel.STABLE -> stableChannelLabel
                        UpdateChannel.RC -> rcChannelLabel
                    }
                },
                label = stringResource(Res.string.settings_update_channel),
                hint = stringResource(Res.string.settings_update_channel_hint),
                allowNone = false,
            )
        }
    }
}