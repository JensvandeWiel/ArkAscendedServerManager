package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.ini.ServerSettings
import eu.wynq.arkascendedservermanager.core.server.defaultValueString
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormOptionalTextField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormToggleableNullableTextField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun ServerOptionsSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val serverOptionsGroupLabel = stringResource(Res.string.server_details_group_server_options)
    val idlePlayerKickTimeHint = stringResource(Res.string.server_details_idle_player_kick_time_hint)
    val idlePlayerKickEnabledLabel = stringResource(Res.string.server_details_idle_player_kick_enabled_label)
    val idlePlayerKickEnabledHint = stringResource(Res.string.server_details_idle_player_kick_enabled_hint)
    val banListUrlLabel = stringResource(Res.string.server_details_ban_list_url_label)
    val banListUrlHint = stringResource(Res.string.server_details_ban_list_url_hint)
    val useDynamicConfigLabel = stringResource(Res.string.server_details_use_dynamic_config_label)
    val useDynamicConfigHint = stringResource(Res.string.server_details_use_dynamic_config_hint)
    val customDynamicConfigUrlHint = stringResource(Res.string.server_details_custom_dynamic_config_url_hint)
    val customLiveTuningUrlLabel = stringResource(Res.string.server_details_custom_live_tuning_url_label)
    val customLiveTuningUrlHint = stringResource(Res.string.server_details_custom_live_tuning_url_hint)
    val slotsLabel = stringResource(Res.string.server_details_slots_label)
    val slotsHint = stringResource(Res.string.server_details_slots_hint)

    model.server?.run {
        GroupHeader(serverOptionsGroupLabel)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FormCheckboxField(
                settings.options.enableIdlePlayerKick,
                onCheckedChange = { newValue ->
                    component.updateServerOptions {
                        it.copy(enableIdlePlayerKick = newValue)
                    }
                },
                label = idlePlayerKickEnabledLabel,
                hint = idlePlayerKickEnabledHint,
            )
            FormSliderField(
                value = gameUserSettings.serverSettings.kickIdlePlayersPeriod,
                onValueChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(kickIdlePlayersPeriod = newValue))
                    }
                },
                label = null,
                valueRange = 0..7200,
                hint = idlePlayerKickTimeHint,
                allowOutsideRange = true,
                showManualInput = true,
                enabled = settings.options.enableIdlePlayerKick,
                error = if (settings.options.enableIdlePlayerKick) !gameUserSettings.serverSettings.validateKickIdlePlayersPeriod() else false,
            )
        }
        FormOptionalTextField(
            value = gameUserSettings.serverSettings.banListUrl,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(banListUrl = newValue))
                }
            },
            defaultValue = ServerSettings::banListUrl.defaultValueString!!,
            label = banListUrlLabel,
            hint = banListUrlHint,
        )
        FormToggleableNullableTextField(
            value = gameUserSettings.serverSettings.customDynamicConfigUrl,
            onValueChange = { newValue: String? ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(customDynamicConfigUrl = newValue))
                }
            },
            enabled = settings.options.useDynamicConfig,
            onEnabledChange = { newValue ->
                component.updateServerOptions {
                    it.copy(useDynamicConfig = newValue)
                }
            },
            label = useDynamicConfigLabel,
            hint = useDynamicConfigHint,
            fieldHint = customDynamicConfigUrlHint,
            error = !gameUserSettings.serverSettings.validateCustomDynamicConfigUrl(),
        )
        FormOptionalTextField(
            value = gameUserSettings.serverSettings.customLiveTuningUrl,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(customLiveTuningUrl = newValue))
                }
            },
            defaultValue = ServerSettings::customLiveTuningUrl.defaultValueString!!,
            label = customLiveTuningUrlLabel,
            hint = customLiveTuningUrlHint,
        )
        FormSliderField(
            value = settings.administration.slots,
            onValueChange = { newValue ->
                component.updateServerAdministrationSettings {
                    it.copy(slots = newValue)
                }
            },
            label = slotsLabel,
            valueRange = 1..250,
            error = !settings.administration.validateSlots(),
            hint = slotsHint,
            allowOutsideRange = true,
            showManualInput = true,
        )
    }
}

