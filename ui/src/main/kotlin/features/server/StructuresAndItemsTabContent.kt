package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding

@Composable
fun StructuresAndItemsTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_structures_limits_group))
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.structureResistanceMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(structureResistanceMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_resistance_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_resistance_multiplier_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.structurePreventResourceRadiusMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(structurePreventResourceRadiusMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_prevent_resource_radius_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_prevent_resource_radius_multiplier_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormSliderField(
                    value = gameUserSettings.serverSettings.theMaxStructuresInRange,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(theMaxStructuresInRange = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_max_structures_in_range_label),
                    hint = stringResource(Res.string.server_details_structures_max_structures_in_range_hint),
                    valueRange = 0..50_000,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.ignoreLimitMaxStructuresInRangeTypeFlag,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(ignoreLimitMaxStructuresInRangeTypeFlag = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_ignore_limit_type_flag_label),
                    hint = stringResource(Res.string.server_details_structures_ignore_limit_type_flag_hint),
                )

                GroupHeader(stringResource(Res.string.server_details_structures_platform_saddles_group))
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.perPlatformMaxStructuresMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(perPlatformMaxStructuresMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_per_platform_max_structures_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_per_platform_max_structures_multiplier_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.platformSaddleBuildAreaBoundsMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(platformSaddleBuildAreaBoundsMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_platform_saddle_build_area_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_platform_saddle_build_area_multiplier_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormSliderField(
                    value = gameUserSettings.serverSettings.maxPlatformSaddleStructureLimit,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(maxPlatformSaddleStructureLimit = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_max_platform_saddle_structure_limit_label),
                    hint = stringResource(Res.string.server_details_structures_max_platform_saddle_structure_limit_hint),
                    valueRange = 0..1_000,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.overrideStructurePlatformPrevention,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(overrideStructurePlatformPrevention = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_override_platform_prevention_label),
                    hint = stringResource(Res.string.server_details_structures_override_platform_prevention_hint),
                )

                GroupHeader(stringResource(Res.string.server_details_structures_pickup_group))
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.alwaysAllowStructurePickup,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(alwaysAllowStructurePickup = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_always_allow_pickup_label),
                    hint = stringResource(Res.string.server_details_structures_always_allow_pickup_hint),
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.structurePickupTimeAfterPlacement,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(structurePickupTimeAfterPlacement = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_pickup_time_after_placement_label),
                    hint = stringResource(Res.string.server_details_structures_pickup_time_after_placement_hint),
                    valueRange = 0.0f..300.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.structurePickupHoldDuration,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(structurePickupHoldDuration = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_pickup_hold_duration_label),
                    hint = stringResource(Res.string.server_details_structures_pickup_hold_duration_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )

                GroupHeader(stringResource(Res.string.server_details_structures_decay_group))
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.disableStructureDecayPvE,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(disableStructureDecayPvE = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_disable_decay_pve_label),
                    hint = stringResource(Res.string.server_details_structures_disable_decay_pve_hint),
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.pvEStructureDecayPeriodMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(pvEStructureDecayPeriodMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_pve_decay_period_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_pve_decay_period_multiplier_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.pvPStructureDecay,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(pvPStructureDecay = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_pvp_decay_label),
                    hint = stringResource(Res.string.server_details_structures_pvp_decay_hint),
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.autoDestroyOldStructuresMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(autoDestroyOldStructuresMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_auto_destroy_old_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_auto_destroy_old_multiplier_hint),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormCheckboxField(
                    checked = settings.options.autoDestroyStructures,
                    onCheckedChange = { newValue ->
                        component.updateServerOptions {
                            it.copy(autoDestroyStructures = newValue)
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_auto_destroy_structures_label),
                    hint = stringResource(Res.string.server_details_structures_auto_destroy_structures_hint),
                )

                GroupHeader(stringResource(Res.string.server_details_structures_turrets_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FormCheckboxField(
                        checked = game.shooterGameMode.hardLimitTurretsInRange,
                        onCheckedChange = { newValue ->
                            component.updateServerGame {
                                it.copy(shooterGameMode = it.shooterGameMode.copy(hardLimitTurretsInRange = newValue))
                            }
                        },
                        label = stringResource(Res.string.server_details_structures_hard_limit_turrets_label),
                        hint = stringResource(Res.string.server_details_structures_hard_limit_turrets_hint),
                        modifier = Modifier.weight(1f),
                    )
                    FormCheckboxField(
                        checked = game.shooterGameMode.limitTurretsInRange,
                        onCheckedChange = { newValue ->
                            component.updateServerGame {
                                it.copy(shooterGameMode = it.shooterGameMode.copy(limitTurretsInRange = newValue))
                            }
                        },
                        label = stringResource(Res.string.server_details_structures_limit_turrets_label),
                        hint = stringResource(Res.string.server_details_structures_limit_turrets_hint),
                        modifier = Modifier.weight(1f),
                    )
                }
                FormSliderField(
                    value = game.shooterGameMode.limitTurretsNum,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(limitTurretsNum = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_limit_turrets_num_label),
                    hint = stringResource(Res.string.server_details_structures_limit_turrets_num_hint),
                    valueRange = 0..1_000,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.limitTurretsRange,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(limitTurretsRange = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_limit_turrets_range_label),
                    hint = stringResource(Res.string.server_details_structures_limit_turrets_range_hint),
                    valueRange = 0.0f..50_000.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )

                GroupHeader(stringResource(Res.string.server_details_structures_items_group))
                FormCheckboxField(
                    checked = gameUserSettings.serverSettings.clampItemStats,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(clampItemStats = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_clamp_item_stats_label),
                    hint = stringResource(Res.string.server_details_structures_clamp_item_stats_hint),
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.itemStackSizeMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(itemStackSizeMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_item_stack_size_multiplier_label),
                    hint = stringResource(Res.string.server_details_structures_item_stack_size_multiplier_hint),
                    valueRange = 0.0f..100.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )

                GroupHeader(stringResource(Res.string.server_details_structures_wireless_crafting_group))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = game.shooterGameMode.disableWirelessCrafting,
                            onCheckedChange = { newValue ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(disableWirelessCrafting = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_structures_disable_wireless_crafting_label),
                            hint = stringResource(Res.string.server_details_structures_disable_wireless_crafting_hint),
                        )
                        FormCheckboxField(
                            checked = game.shooterGameMode.disableWirelessCraftingForPlayers,
                            onCheckedChange = { newValue ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(disableWirelessCraftingForPlayers = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_structures_disable_wireless_crafting_players_label),
                            hint = stringResource(Res.string.server_details_structures_disable_wireless_crafting_players_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            checked = game.shooterGameMode.disableWirelessCraftingForDinos,
                            onCheckedChange = { newValue ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(disableWirelessCraftingForDinos = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_structures_disable_wireless_crafting_dinos_label),
                            hint = stringResource(Res.string.server_details_structures_disable_wireless_crafting_dinos_hint),
                        )
                        FormCheckboxField(
                            checked = game.shooterGameMode.disableWirelessCraftingForStructures,
                            onCheckedChange = { newValue ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(disableWirelessCraftingForStructures = newValue))
                                }
                            },
                            label = stringResource(Res.string.server_details_structures_disable_wireless_crafting_structures_label),
                            hint = stringResource(Res.string.server_details_structures_disable_wireless_crafting_structures_hint),
                        )
                    }
                }
                FormSliderField(
                    value = game.shooterGameMode.wirelessCraftingRangeOverride,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(wirelessCraftingRangeOverride = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_wireless_crafting_range_label),
                    hint = stringResource(Res.string.server_details_structures_wireless_crafting_range_hint),
                    valueRange = 0..10_000,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )

                GroupHeader(stringResource(Res.string.server_details_structures_generators_group))
                FormSliderField(
                    value = game.shooterGameMode.limitGeneratorsNum,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(limitGeneratorsNum = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_limit_generators_num_label),
                    hint = stringResource(Res.string.server_details_structures_limit_generators_num_hint),
                    valueRange = 0..100,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
                FormSliderField(
                    value = game.shooterGameMode.limitGeneratorsRange,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(limitGeneratorsRange = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_structures_limit_generators_range_label),
                    hint = stringResource(Res.string.server_details_structures_limit_generators_range_hint),
                    valueRange = 0..50_000,
                    allowOutsideRange = true,
                    showManualInput = true,
                    labelPosition = LabelPosition.Above,
                )
            }
        }
    }
}
