package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.server.Administration
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormNullableTextField
import eu.wynq.arkascendedservermanager.ui.components.FormOptionalSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormTextarea
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import eu.wynq.arkascendedservermanager.ui.components.optionScrollState
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding

@Composable
fun AdvancedAndLaunchTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer(scrollState = optionScrollState()) {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_advanced_logging_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.serverGameLog,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(serverGameLog = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_server_game_log_label),
                            hint = stringResource(Res.string.server_details_advanced_server_game_log_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.serverGameLogIncludeTribeLogs,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(serverGameLogIncludeTribeLogs = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_include_tribe_logs_label),
                            hint = stringResource(Res.string.server_details_advanced_include_tribe_logs_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.serverRconOutputTribeLogs,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(serverRconOutputTribeLogs = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_rcon_tribe_logs_label),
                            hint = stringResource(Res.string.server_details_advanced_rcon_tribe_logs_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.log,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(log = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_log_label),
                            hint = stringResource(Res.string.server_details_advanced_log_hint),
                        )
                    }
                }

                GroupHeader(stringResource(Res.string.server_details_advanced_performance_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.noAi,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noAi = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_ai_label),
                            hint = stringResource(Res.string.server_details_advanced_no_ai_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.noSound,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noSound = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_sound_label),
                            hint = stringResource(Res.string.server_details_advanced_no_sound_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.oneThread,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(oneThread = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_one_thread_label),
                            hint = stringResource(Res.string.server_details_advanced_one_thread_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.noPerfThreads,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noPerfThreads = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_perf_threads_label),
                            hint = stringResource(Res.string.server_details_advanced_no_perf_threads_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.forceUsePerfThreads,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(forceUsePerfThreads = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_force_perf_threads_label),
                            hint = stringResource(Res.string.server_details_advanced_force_perf_threads_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.disableInOneRangeScaling,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(disableInOneRangeScaling = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_disable_replication_scaling_label),
                            hint = stringResource(Res.string.server_details_advanced_disable_replication_scaling_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.alwaysTickDedicatedSkeletalMeshes,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(alwaysTickDedicatedSkeletalMeshes = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_always_tick_meshes_label),
                            hint = stringResource(Res.string.server_details_advanced_always_tick_meshes_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.stasisKeepControllers,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(stasisKeepControllers = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_stasis_controllers_label),
                            hint = stringResource(Res.string.server_details_advanced_stasis_controllers_hint),
                        )
                    }
                }

                GroupHeader(stringResource(Res.string.server_details_advanced_stability_group))
                FormCheckboxField(
                    checked = settings.administration.gbUsageToForceRestart != null,
                    onCheckedChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(gbUsageToForceRestart = if (newValue) 35.0f else null)
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_memory_guard_label),
                    hint = stringResource(Res.string.server_details_advanced_memory_guard_hint),
                )
                FormFloatSliderField(
                    value = settings.administration.gbUsageToForceRestart ?: 35.0f,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(gbUsageToForceRestart = newValue)
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_memory_guard_gb_label),
                    hint = stringResource(Res.string.server_details_advanced_memory_guard_gb_hint),
                    valueRange = 0.0f..128.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    enabled = settings.administration.gbUsageToForceRestart != null,
                    labelPosition = LabelPosition.Above,
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FormCheckboxField(
                        checked = settings.options.noHangDetection,
                        onCheckedChange = { newValue ->
                            component.updateServerOptions { it.copy(noHangDetection = newValue) }
                        },
                        label = stringResource(Res.string.server_details_advanced_no_hang_detection_label),
                        hint = stringResource(Res.string.server_details_advanced_no_hang_detection_hint),
                        modifier = Modifier.weight(1f),
                    )
                    FormCheckboxField(
                        checked = settings.options.preventHibernation,
                        onCheckedChange = { newValue ->
                            component.updateServerOptions { it.copy(preventHibernation = newValue) }
                        },
                        label = stringResource(Res.string.server_details_advanced_prevent_hibernation_label),
                        hint = stringResource(Res.string.server_details_advanced_prevent_hibernation_hint),
                        modifier = Modifier.weight(1f),
                    )
                }

                GroupHeader(stringResource(Res.string.server_details_advanced_save_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FormCheckboxField(
                        checked = settings.options.useStore,
                        onCheckedChange = { newValue ->
                            component.updateServerOptions { it.copy(useStore = newValue) }
                        },
                        label = stringResource(Res.string.server_details_advanced_use_store_label),
                        hint = stringResource(Res.string.server_details_advanced_use_store_hint),
                        modifier = Modifier.weight(1f),
                    )
                    FormCheckboxField(
                        checked = settings.options.convertToStore,
                        onCheckedChange = { newValue ->
                            component.updateServerOptions { it.copy(convertToStore = newValue) }
                        },
                        label = stringResource(Res.string.server_details_advanced_convert_to_store_label),
                        hint = stringResource(Res.string.server_details_advanced_convert_to_store_hint),
                        modifier = Modifier.weight(1f),
                    )
                }
                FormOptionalSliderField(
                    value = settings.administration.maxNumOfSaveBackups,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(maxNumOfSaveBackups = newValue)
                        }
                    },
                    defaultValue = 20,
                    label = stringResource(Res.string.server_details_advanced_max_save_backups_label),
                    hint = stringResource(Res.string.server_details_advanced_max_save_backups_hint),
                    valueRange = 0..100,
                    allowOutsideRange = true,
                    showManualInput = true,
                )
                FormNullableTextField(
                    value = settings.administration.altSaveDirectoryName,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(altSaveDirectoryName = newValue?.takeIf { value -> value.isNotBlank() })
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_alt_save_dir_label),
                    hint = stringResource(Res.string.server_details_advanced_alt_save_dir_hint),
                    error = !settings.administration.validateAltSaveDirectoryName(),
                )

                GroupHeader(stringResource(Res.string.server_details_advanced_dino_spawn_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.noWildBabies,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noWildBabies = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_wild_babies_label),
                            hint = stringResource(Res.string.server_details_advanced_no_wild_babies_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.noDinosExceptForcedSpawn,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noDinosExceptForcedSpawn = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_dinos_forced_label),
                            hint = stringResource(Res.string.server_details_advanced_no_dinos_forced_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.noDinosExceptStreamingSpawn,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noDinosExceptStreamingSpawn = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_dinos_streaming_label),
                            hint = stringResource(Res.string.server_details_advanced_no_dinos_streaming_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = settings.options.noDinosExceptManualSpawn,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noDinosExceptManualSpawn = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_dinos_manual_label),
                            hint = stringResource(Res.string.server_details_advanced_no_dinos_manual_hint),
                        )
                        FormCheckboxField(
                            checked = settings.options.noDinosExceptWaterSpawn,
                            onCheckedChange = { newValue ->
                                component.updateServerOptions { it.copy(noDinosExceptWaterSpawn = newValue) }
                            },
                            label = stringResource(Res.string.server_details_advanced_no_dinos_water_label),
                            hint = stringResource(Res.string.server_details_advanced_no_dinos_water_hint),
                        )
                    }
                }

                GroupHeader(stringResource(Res.string.server_details_advanced_network_group))
                FormCheckboxField(
                    checked = settings.options.multiHome,
                    onCheckedChange = { newValue ->
                        component.updateServerOptions { it.copy(multiHome = newValue) }
                    },
                    label = stringResource(Res.string.server_details_advanced_multihome_label),
                    hint = stringResource(Res.string.server_details_advanced_multihome_hint),
                )
                FormNullableTextField(
                    value = gameUserSettings.sessionSettings.multiHome,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(sessionSettings = it.sessionSettings.copy(multiHome = newValue?.takeIf { value -> value.isNotBlank() }))
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_multihome_ip_label),
                    hint = stringResource(Res.string.server_details_advanced_multihome_ip_hint),
                    enabled = settings.options.multiHome,
                    error = !gameUserSettings.sessionSettings.validateMultiHome(),
                )
                FormNullableTextField(
                    value = settings.administration.serverIp,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(serverIp = newValue?.takeIf { value -> value.isNotBlank() })
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_server_ip_label),
                    hint = stringResource(Res.string.server_details_advanced_server_ip_hint),
                    error = !settings.administration.validateServerIp(),
                )
                FormNullableTextField(
                    value = settings.administration.ip,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(ip = newValue?.takeIf { value -> value.isNotBlank() })
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_ip_label),
                    hint = stringResource(Res.string.server_details_advanced_ip_hint),
                    error = !settings.administration.validateIp(),
                )
                FormNullableTextField(
                    value = settings.administration.serverPlatform,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(serverPlatform = newValue?.takeIf { value -> value.isNotBlank() })
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_platform_label),
                    hint = stringResource(Res.string.server_details_advanced_platform_hint),
                    error = !settings.administration.validateServerPlatform(),
                )
                FormCheckboxField(
                    checked = settings.options.nitradoQueryPort,
                    onCheckedChange = { newValue ->
                        component.updateServerOptions { it.copy(nitradoQueryPort = newValue) }
                    },
                    label = stringResource(Res.string.server_details_advanced_nitrado_query_port_label),
                    hint = stringResource(Res.string.server_details_advanced_nitrado_query_port_hint),
                )

                GroupHeader(stringResource(Res.string.server_details_advanced_mods_group))
                FormTextField(
                    value = settings.options.passiveMods.joinToString(","),
                    onValueChange = { newValue ->
                        val mods = newValue.split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                        component.updateServerOptions { it.copy(passiveMods = mods) }
                    },
                    label = stringResource(Res.string.server_details_advanced_passive_mods_label),
                    hint = stringResource(Res.string.server_details_advanced_passive_mods_hint),
                )

                GroupHeader(stringResource(Res.string.server_details_advanced_notifications_group))
                FormNullableTextField(
                    value = settings.administration.customNotificationUrl,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(customNotificationUrl = newValue?.takeIf { value -> value.isNotBlank() })
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_notification_url_label),
                    hint = stringResource(Res.string.server_details_advanced_notification_url_hint),
                    error = !settings.administration.validateCustomNotificationUrl(),
                )

                GroupHeader(stringResource(Res.string.server_details_advanced_extra_group))
                FormTextarea(
                    value = settings.administration.extraArgs ?: "",
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(extraArgs = newValue.takeIf { value -> value.isNotBlank() })
                        }
                    },
                    label = stringResource(Res.string.server_details_advanced_extra_args_label),
                    hint = stringResource(Res.string.server_details_advanced_extra_args_hint),
                )
            }
        }
    }
}

