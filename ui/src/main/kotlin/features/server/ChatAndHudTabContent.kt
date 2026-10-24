package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormNullableTextField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding

@Composable
fun ChatAndHudTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_chat_and_hud_chat_group))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.adminLogging,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(adminLogging = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_admin_logging_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_admin_logging_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.globalVoiceChat,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(globalVoiceChat = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_global_voice_chat_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_global_voice_chat_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.proximityChat,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(proximityChat = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_proximity_chat_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_proximity_chat_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.dontAlwaysNotifyPlayerJoined,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(dontAlwaysNotifyPlayerJoined = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_dont_always_notify_player_joined_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_dont_always_notify_player_joined_hint),
                        )
                    }
                }

                GroupHeader(stringResource(Res.string.server_details_chat_and_hud_word_filter_group))
                FormNullableTextField(
                    value = gameUserSettings.serverSettings.badWordListUrl,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(
                                serverSettings = it.serverSettings.copy(
                                    badWordListUrl = newValue?.takeIf { value -> value.isNotBlank() }
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_chat_and_hud_bad_word_list_url_label),
                    hint = stringResource(Res.string.server_details_chat_and_hud_bad_word_list_url_hint),
                    error = !gameUserSettings.serverSettings.validateBadWordListUrl(),
                )
                FormNullableTextField(
                    value = gameUserSettings.serverSettings.badWordWhiteListUrl,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(
                                serverSettings = it.serverSettings.copy(
                                    badWordWhiteListUrl = newValue?.takeIf { value -> value.isNotBlank() }
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_chat_and_hud_bad_word_whitelist_url_label),
                    hint = stringResource(Res.string.server_details_chat_and_hud_bad_word_whitelist_url_hint),
                    error = !gameUserSettings.serverSettings.validateBadWordWhiteListUrl(),
                )

                GroupHeader(stringResource(Res.string.server_details_chat_and_hud_hud_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.serverCrosshair,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(serverCrosshair = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_server_crosshair_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_server_crosshair_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.showMapPlayerLocation,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(showMapPlayerLocation = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_show_map_player_location_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_show_map_player_location_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.allowThirdPersonPlayer,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowThirdPersonPlayer = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_allow_third_person_player_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_allow_third_person_player_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.allowHitMarkers,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowHitMarkers = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_allow_hit_markers_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_allow_hit_markers_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.allowHideDamageSourceFromLogs,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowHideDamageSourceFromLogs = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_allow_hide_damage_source_from_logs_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_allow_hide_damage_source_from_logs_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.showFloatingDamageText,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(showFloatingDamageText = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_show_floating_damage_text_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_show_floating_damage_text_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.serverForceNoHud,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(serverForceNoHud = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_server_force_no_hud_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_server_force_no_hud_hint),
                        )
                        FormCheckboxField(
                            checked = gameUserSettings.serverSettings.preventSpawnAnimations,
                            onCheckedChange = { newValue ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(preventSpawnAnimations = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_chat_and_hud_prevent_spawn_animations_label),
                            hint = stringResource(Res.string.server_details_chat_and_hud_prevent_spawn_animations_hint),
                        )
                    }
                }

                GroupHeader(stringResource(Res.string.server_details_chat_and_hud_gamma_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FormCheckboxField(
                        checked = gameUserSettings.serverSettings.disablePvEGamma,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(disablePvEGamma = newValue))
                            }
                        },
                        label = stringResource(Res.string.server_details_chat_and_hud_disable_pve_gamma_label),
                        hint = stringResource(Res.string.server_details_chat_and_hud_disable_pve_gamma_hint),
                        modifier = Modifier.weight(1f),
                    )
                    FormCheckboxField(
                        checked = gameUserSettings.serverSettings.enablePvPGamma,
                        onCheckedChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(enablePvPGamma = newValue))
                            }
                        },
                        label = stringResource(Res.string.server_details_chat_and_hud_enable_pvp_gamma_label),
                        hint = stringResource(Res.string.server_details_chat_and_hud_enable_pvp_gamma_hint),
                        modifier = Modifier.weight(1f),
                    )
                }

                GroupHeader(stringResource(Res.string.server_details_chat_and_hud_photo_mode_group))
                FormCheckboxField(
                    checked = game.shooterGameMode.disablePhotoMode,
                    onCheckedChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(disablePhotoMode = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_chat_and_hud_disable_photo_mode_label),
                    hint = stringResource(Res.string.server_details_chat_and_hud_disable_photo_mode_hint),
                )
                FormSliderField(
                    value = game.shooterGameMode.photoModeRangeLimit,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(photoModeRangeLimit = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_chat_and_hud_photo_mode_range_limit_label),
                    hint = stringResource(Res.string.server_details_chat_and_hud_photo_mode_range_limit_hint),
                    valueRange = 0..10_000,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = LabelPosition.Above,
                )
            }
        }
    }
}
