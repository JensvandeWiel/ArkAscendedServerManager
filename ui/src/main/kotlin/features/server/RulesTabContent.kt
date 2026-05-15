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
                                    it.copy(shooterGameMode = it.shooterGameMode.copy(useSingleplayerSettings = newval))
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
            }
        }
    }
}