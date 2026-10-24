package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import arkascendedservermanager.ui.generated.resources.*
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun RulesTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_rules_tab))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            gameUserSettings.serverSettings.serverHardcore,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(serverHardcore = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_enable_hardcore_mode_label),
                            hint = stringResource(Res.string.server_details_rules_enable_hardcore_mode_hint)
                        )
                        FormCheckboxField(
                            game.shooterGameMode.disableFriendlyFire,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(disableFriendlyFire = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_disable_pvp_friendly_fire_label),
                            hint = stringResource(Res.string.server_details_rules_disable_pvp_friendly_fire_hint)
                        )
                        FormCheckboxField(
                            game.shooterGameMode.pvEDisableFriendlyFire,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(pvEDisableFriendlyFire = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_disable_pve_friendly_fire_label),
                            hint = stringResource(Res.string.server_details_rules_disable_pve_friendly_fire_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.enableExtraStructurePreventionVolumes,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(enableExtraStructurePreventionVolumes = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_prevent_building_resource_rich_areas_label),
                            hint = stringResource(Res.string.server_details_rules_prevent_building_resource_rich_areas_hint)
                        )
                        FormCheckboxField(
                            game.shooterGameMode.disableLootCrates,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(disableLootCrates = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_disable_supply_drops_label),
                            hint = stringResource(Res.string.server_details_rules_disable_supply_drops_hint)
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            gameUserSettings.serverSettings.serverPVE,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(serverPVE = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_enable_pve_label),
                            hint = stringResource(Res.string.server_details_rules_enable_pve_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.allowCaveBuildingPvE,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowCaveBuildingPvE = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_enable_pve_cave_building_label),
                            hint = stringResource(Res.string.server_details_rules_enable_pve_cave_building_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.allowCaveBuildingPvP,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowCaveBuildingPvP = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_enable_pvp_cave_building_label),
                            hint = stringResource(Res.string.server_details_rules_enable_pvp_cave_building_hint)
                        )
                        FormCheckboxField(
                            game.shooterGameMode.useSingleplayerSettings,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(useSingleplayerSettings = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_use_singleplayer_settings_label),
                            hint = stringResource(Res.string.server_details_rules_use_singleplayer_settings_hint)
                        )
                        FormCheckboxField(
                            !gameUserSettings.serverSettings.preventTribeAlliances,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(preventTribeAlliances = !newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_prevent_tribe_alliances_label),
                            hint = stringResource(Res.string.server_details_rules_prevent_tribe_alliances_hint),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            game.shooterGameMode.showCreativeMode,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(showCreativeMode = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_allow_creative_mode_label),
                            hint = stringResource(Res.string.server_details_rules_allow_creative_mode_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.enableCryoSicknessPVE,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(enableCryoSicknessPVE = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_enable_pve_cryo_sickness_label),
                            hint = stringResource(Res.string.server_details_rules_enable_pve_cryo_sickness_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.randomSupplyCratePoints,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(randomSupplyCratePoints = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_randomize_supply_crate_points_label),
                            hint = stringResource(Res.string.server_details_rules_randomize_supply_crate_points_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.allowCrateSpawnsOnTopOfStructures,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowCrateSpawnsOnTopOfStructures = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_allow_crate_spawns_on_structures_label),
                            hint = stringResource(Res.string.server_details_rules_allow_crate_spawns_on_structures_hint)
                        )
                    }
                }

                CheckboxSectionHeader(
                    !gameUserSettings.serverSettings.preventDiseases,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(preventDiseases = !newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_enable_diseases_label),
                    hint = stringResource(Res.string.server_details_rules_enable_diseases_hint),
                )
                FormCheckboxField(
                    gameUserSettings.serverSettings.nonPermanentDiseases,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(nonPermanentDiseases = newval))
                        }
                    },
                    enabled = !gameUserSettings.serverSettings.preventDiseases,
                    label = stringResource(Res.string.server_details_rules_non_permanent_diseases_label),
                    hint = stringResource(Res.string.server_details_rules_non_permanent_diseases_hint)
                )
                GroupHeader(stringResource(Res.string.server_details_rules_tribes_header))
                FormSliderField(
                    value = game.shooterGameMode.maxNumberOfPlayersInTribe,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(maxNumberOfPlayersInTribe = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_max_players_in_tribe_label),
                    hint = stringResource(Res.string.server_details_rules_max_players_in_tribe_hint),
                    valueRange = 0..100,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                FormSliderField(
                    value = gameUserSettings.serverSettings.tribeNameChangeCooldown,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(tribeNameChangeCooldown = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_tribe_name_change_cooldown_label),
                    hint = stringResource(Res.string.server_details_rules_tribe_name_change_cooldown_hint),
                    valueRange = 0..2880,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                FormSliderField(
                    value = game.shooterGameMode.tribeSlotReuseCooldown / 60,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(tribeSlotReuseCooldown = newval * 60))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_tribe_slot_reuse_cooldown_label),
                    hint = stringResource(Res.string.server_details_rules_tribe_slot_reuse_cooldown_hint),
                    valueRange = 0..60,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                
                // Nested alliances section
                CheckboxSectionHeader(
                    !gameUserSettings.serverSettings.preventTribeAlliances,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(preventTribeAlliances = !newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_prevent_tribe_alliances_label),
                    hint = stringResource(Res.string.server_details_rules_prevent_tribe_alliances_hint),
                )
                FormSliderField(
                    value = game.shooterGameMode.maxAlliancesPerTribe,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(maxAlliancesPerTribe = newval))
                        }
                    },
                    enabled = !gameUserSettings.serverSettings.preventTribeAlliances,
                    label = stringResource(Res.string.server_details_rules_max_alliances_per_tribe_label),
                    hint = stringResource(Res.string.server_details_rules_max_alliances_per_tribe_hint),
                    valueRange = 0..100,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                FormSliderField(
                    value = game.shooterGameMode.maxTribesPerAlliance,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(maxTribesPerAlliance = newval))
                        }
                    },
                    enabled = !gameUserSettings.serverSettings.preventTribeAlliances,
                    label = stringResource(Res.string.server_details_rules_max_tribes_per_alliance_label),
                    hint = stringResource(Res.string.server_details_rules_max_tribes_per_alliance_hint),
                    valueRange = 0..100,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )

                FormCheckboxField(
                    game.shooterGameMode.pveAllowTribeWar,
                    onCheckedChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(pveAllowTribeWar = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_allow_tribe_warfare_label),
                    hint = stringResource(Res.string.server_details_rules_allow_tribe_warfare_hint)
                )
                FormCheckboxField(
                    game.shooterGameMode.pveAllowTribeWarCancel,
                    onCheckedChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(pveAllowTribeWarCancel = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_allow_tribe_warfare_cancel_label),
                    hint = stringResource(Res.string.server_details_rules_allow_tribe_warfare_cancel_hint)
                )
                CheckboxSectionHeader(
                    game.shooterGameMode.allowCustomRecipes,
                    onCheckedChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(allowCustomRecipes = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_allow_custom_recipes_label),
                    hint = stringResource(Res.string.server_details_rules_allow_custom_recipes_hint)
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.customRecipeEffectivenessMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(customRecipeEffectivenessMultiplier = newval))
                        }
                    },
                    enabled = game.shooterGameMode.allowCustomRecipes,
                    label = stringResource(Res.string.server_details_rules_custom_recipe_effectiveness_multiplier_label),
                    hint = stringResource(Res.string.server_details_rules_custom_recipe_effectiveness_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.customRecipeSkillMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(customRecipeSkillMultiplier = newval))
                        }
                    },
                    enabled = game.shooterGameMode.allowCustomRecipes,
                    label = stringResource(Res.string.server_details_rules_custom_recipe_skill_multiplier_label),
                    hint = stringResource(Res.string.server_details_rules_custom_recipe_skill_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                GroupHeader(stringResource(Res.string.server_details_rules_offline_protection_header))
                FormCheckboxField(
                    gameUserSettings.serverSettings.preventOfflinePvP,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(preventOfflinePvP = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_prevent_offline_pvp_label),
                    hint = stringResource(Res.string.server_details_rules_prevent_offline_pvp_hint)
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.preventOfflinePvPInterval,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(preventOfflinePvPInterval = newval))
                        }
                    },
                    enabled = gameUserSettings.serverSettings.preventOfflinePvP,
                    label = stringResource(Res.string.server_details_rules_offline_pvp_interval_label),
                    hint = stringResource(Res.string.server_details_rules_offline_pvp_interval_hint),
                    valueRange = 0.0f..3600.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )

                GroupHeader(stringResource(Res.string.server_details_rules_pvp_extras_header))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            gameUserSettings.serverSettings.allowMultipleAttachedC4,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowMultipleAttachedC4 = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_allow_multiple_c4_label),
                            hint = stringResource(Res.string.server_details_rules_allow_multiple_c4_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.pvpDinoDecay,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(pvpDinoDecay = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_pvp_dino_decay_label),
                            hint = stringResource(Res.string.server_details_rules_pvp_dino_decay_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.ignorePvpMountedWeaponryRestrictions,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(ignorePvpMountedWeaponryRestrictions = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_ignore_mounted_weapons_label),
                            hint = stringResource(Res.string.server_details_rules_ignore_mounted_weapons_hint)
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            gameUserSettings.serverSettings.forceAllStructureLocking,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(forceAllStructureLocking = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_force_structure_locking_label),
                            hint = stringResource(Res.string.server_details_rules_force_structure_locking_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.preventMateBoost,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(preventMateBoost = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_prevent_mate_boost_label),
                            hint = stringResource(Res.string.server_details_rules_prevent_mate_boost_hint)
                        )
                        FormCheckboxField(
                            gameUserSettings.serverSettings.allowFlyingStaminaRecovery,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowFlyingStaminaRecovery = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_flying_stamina_recovery_label),
                            hint = stringResource(Res.string.server_details_rules_flying_stamina_recovery_hint)
                        )
                    }
                }

                GroupHeader(stringResource(Res.string.server_details_rules_decay_header))
                FormCheckboxField(
                    gameUserSettings.serverSettings.disableDinoDecayPvE,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(disableDinoDecayPvE = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_disable_dino_decay_pve_label),
                    hint = stringResource(Res.string.server_details_rules_disable_dino_decay_pve_hint)
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.pveDinoDecayPeriodMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(pveDinoDecayPeriodMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_pve_dino_decay_period_label),
                    hint = stringResource(Res.string.server_details_rules_pve_dino_decay_period_hint),
                    valueRange = 0.0f..10.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )

                GroupHeader(stringResource(Res.string.server_details_rules_structures_header))
                FormCheckboxField(
                    gameUserSettings.serverSettings.pveAllowStructuresAtSupplyDrops,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(pveAllowStructuresAtSupplyDrops = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_allow_structures_supply_drops_label),
                    hint = stringResource(Res.string.server_details_rules_allow_structures_supply_drops_hint)
                )
                FormCheckboxField(
                    game.shooterGameMode.disableStructurePlacementCollision,
                    onCheckedChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(disableStructurePlacementCollision = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_disable_structure_collision_label),
                    hint = stringResource(Res.string.server_details_rules_disable_structure_collision_hint)
                )
                FormCheckboxField(
                    game.shooterGameMode.ignoreStructuresPreventionVolumes,
                    onCheckedChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(ignoreStructuresPreventionVolumes = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_rules_ignore_prevention_volumes_label),
                    hint = stringResource(Res.string.server_details_rules_ignore_prevention_volumes_hint)
                )

                GroupHeader(stringResource(Res.string.server_details_rules_leveling_header))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            game.shooterGameMode.allowSpeedLeveling,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(allowSpeedLeveling = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_allow_speed_leveling_label),
                            hint = stringResource(Res.string.server_details_rules_allow_speed_leveling_hint)
                        )
                        FormCheckboxField(
                            game.shooterGameMode.allowFlyerSpeedLeveling,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(allowFlyerSpeedLeveling = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_allow_flyer_speed_leveling_label),
                            hint = stringResource(Res.string.server_details_rules_allow_flyer_speed_leveling_hint)
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            game.shooterGameMode.allowUnlimitedRespecs,
                            onCheckedChange = { newval ->
                                component.updateServerGame {
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(allowUnlimitedRespecs = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_allow_unlimited_respecs_label),
                            hint = stringResource(Res.string.server_details_rules_allow_unlimited_respecs_hint)
                        )
                        FormCheckboxField(
                            settings.options.forceAllowCaveFlyers,
                            onCheckedChange = { newval ->
                                component.updateServerOptions {
                                    it.copy(forceAllowCaveFlyers = newval)
                                }
                            },
                            label = stringResource(Res.string.server_details_rules_force_cave_flyers_label),
                            hint = stringResource(Res.string.server_details_rules_force_cave_flyers_hint)
                        )
                    }
                }
            }
        }
    }
}