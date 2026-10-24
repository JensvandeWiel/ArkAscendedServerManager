package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.ini.ServerSettings
import eu.wynq.arkascendedservermanager.core.server.defaultValueString
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormNullableTextField
import eu.wynq.arkascendedservermanager.ui.components.FormOptionalTextField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding

@Composable
fun AdminAndSecurityTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_admin_and_security_lists_group))
                FormOptionalTextField(
                    value = gameUserSettings.serverSettings.banListUrl,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(banListUrl = newValue))
                        }
                    },
                    defaultValue = ServerSettings::banListUrl.defaultValueString!!,
                    label = stringResource(Res.string.server_details_ban_list_url_label),
                    hint = stringResource(Res.string.server_details_ban_list_url_hint),
                )
                FormNullableTextField(
                    value = gameUserSettings.serverSettings.adminListUrl,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(
                                serverSettings = it.serverSettings.copy(
                                    adminListUrl = newValue?.takeIf { value -> value.isNotBlank() }
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_admin_list_url_label),
                    hint = stringResource(Res.string.server_details_admin_list_url_hint),
                    error = !gameUserSettings.serverSettings.validateAdminListUrl(),
                )
                FormNullableTextField(
                    value = gameUserSettings.serverSettings.cosmeticWhitelistOverride,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(
                                serverSettings = it.serverSettings.copy(
                                    cosmeticWhitelistOverride = newValue?.takeIf { value -> value.isNotBlank() }
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_cosmetic_whitelist_override_label),
                    hint = stringResource(Res.string.server_details_cosmetic_whitelist_override_hint),
                    error = !gameUserSettings.serverSettings.validateCosmeticWhitelistOverride(),
                )

                GroupHeader(stringResource(Res.string.server_details_admin_and_security_tracking_group))
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.useCharacterTracker,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(useCharacterTracker = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_use_character_tracker_label),
                    hint = stringResource(Res.string.server_details_use_character_tracker_hint),
                )

                GroupHeader(stringResource(Res.string.server_details_admin_and_security_cheat_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.useItemDupeCheck,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(useItemDupeCheck = newValue) }
                            },
                            label = stringResource(Res.string.server_details_use_item_dupe_check_label),
                            hint = stringResource(Res.string.server_details_use_item_dupe_check_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.forceDupeLog,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(forceDupeLog = newValue) }
                            },
                            label = stringResource(Res.string.server_details_force_dupe_log_label),
                            hint = stringResource(Res.string.server_details_force_dupe_log_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.disableDupeLogDeletes,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(disableDupeLogDeletes = newValue) }
                            },
                            label = stringResource(Res.string.server_details_disable_dupe_log_deletes_label),
                            hint = stringResource(Res.string.server_details_disable_dupe_log_deletes_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.ignoreDupedItems,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(ignoreDupedItems = newValue) }
                            },
                            label = stringResource(Res.string.server_details_ignore_duped_items_label),
                            hint = stringResource(Res.string.server_details_ignore_duped_items_hint),
                        )
                    }
                }
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.updateAllowedCheatersInterval,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(updateAllowedCheatersInterval = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_update_allowed_cheaters_interval_label),
                    hint = stringResource(Res.string.server_details_update_allowed_cheaters_interval_hint),
                    valueRange = 0.0f..3600.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )

                GroupHeader(stringResource(Res.string.server_details_admin_and_security_mesh_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.serverEnableMeshChecking,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(serverEnableMeshChecking = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_server_enable_mesh_checking_label),
                            hint = stringResource(Res.string.server_details_server_enable_mesh_checking_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.enableMeshBitingProtection,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(enableMeshBitingProtection = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_enable_mesh_biting_protection_label),
                            hint = stringResource(Res.string.server_details_enable_mesh_biting_protection_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.noUnderMeshChecking,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noUnderMeshChecking = newValue) }
                            },
                            label = stringResource(Res.string.server_details_no_under_mesh_checking_label),
                            hint = stringResource(Res.string.server_details_no_under_mesh_checking_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.noUnderMeshKilling,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noUnderMeshKilling = newValue) }
                            },
                            label = stringResource(Res.string.server_details_no_under_mesh_killing_label),
                            hint = stringResource(Res.string.server_details_no_under_mesh_killing_hint),
                        )
                    }
                }
            }
        }
    }
}
