package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import arkascendedservermanager.ui.generated.resources.*
import eu.wynq.arkascendedservermanager.core.ini.ShooterGameMode
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun DinoSettingsTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_dino_settings_tab))

                GroupHeader(stringResource(Res.string.server_details_dino_raid_settings_header))
                FormCheckboxField(
                    gameUserSettings.serverSettings.allowRaidDinoFeeding,
                    onCheckedChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(allowRaidDinoFeeding = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_allow_raid_dino_feeding_label),
                    hint = stringResource(Res.string.server_details_dino_allow_raid_dino_feeding_hint)
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.raidDinoCharacterFoodDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(raidDinoCharacterFoodDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_raid_food_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_raid_food_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                GroupHeader(stringResource(Res.string.server_details_dino_tame_limits_header))
                FormSliderField(
                    value = gameUserSettings.serverSettings.maxPersonalTamedDinos,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(maxPersonalTamedDinos = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_max_personal_tamed_dinos_label),
                    hint = stringResource(Res.string.server_details_dino_max_personal_tamed_dinos_hint),
                    valueRange = 0..10000,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )
                FormSliderField(
                    value = gameUserSettings.serverSettings.maxTamedDinos,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(maxTamedDinos = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_max_tamed_dinos_label),
                    hint = stringResource(Res.string.server_details_dino_max_tamed_dinos_hint),
                    valueRange = 0..10000,
                    showManualInput = true,
                    allowOutsideRange = true,
                    labelPosition = eu.wynq.arkascendedservermanager.ui.components.LabelPosition.Above
                )

                GroupHeader(stringResource(Res.string.server_details_dino_tamed_header))
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.dinoCharacterHealthRecoveryMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(dinoCharacterHealthRecoveryMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_health_recovery_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_health_recovery_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.dinoCharacterStaminaDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(dinoCharacterStaminaDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_stamina_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_stamina_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.tamedDinoDamageMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(tamedDinoDamageMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_tamed_damage_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_tamed_damage_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.tamedDinoResistanceMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(tamedDinoResistanceMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_tamed_resistance_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_tamed_resistance_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.tamedDinoCharacterFoodDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(tamedDinoCharacterFoodDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_tamed_food_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_tamed_food_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.tamedDinoTorporDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(tamedDinoTorporDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_tamed_torpor_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_tamed_torpor_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.dinoHarvestingDamageMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(dinoHarvestingDamageMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_harvesting_damage_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_harvesting_damage_multiplier_hint),
                    valueRange = 0.1f..10.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.passiveTameIntervalMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(passiveTameIntervalMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_passive_tame_interval_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_passive_tame_interval_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                GroupHeader(stringResource(Res.string.server_details_dino_wild_header))
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.dinoDamageMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(dinoDamageMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_damage_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_damage_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.dinoResistanceMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(dinoResistanceMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_resistance_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_resistance_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.wildDinoCharacterFoodDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(wildDinoCharacterFoodDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_wild_food_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_wild_food_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.wildDinoTorporDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(wildDinoTorporDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_wild_torpor_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_wild_torpor_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                GroupHeader(stringResource(Res.string.server_details_dino_other_header))
                FormFloatSliderField(
                    value = game.shooterGameMode.dinoTurretDamageMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(dinoTurretDamageMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_turret_damage_multiplier_label),
                    hint = stringResource(Res.string.server_details_dino_turret_damage_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                GroupHeader(stringResource(Res.string.server_details_dino_per_level_stats_header))

                CheckboxSectionHeader(
                    checked = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null,
                    onCheckedChange = { newVal ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = if (newVal) ShooterGameMode.createPerLevelStatsMultiplierDinoTamedDefault() else null))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stats_tamed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stats_tamed_hint)
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(0) ?: 0.2f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[0] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_health_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_health_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(1) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[1] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stamina_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stamina_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(2) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[2] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_torpidity_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_torpidity_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(3) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[3] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_oxygen_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_oxygen_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(4) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[4] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_food_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_food_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(5) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[5] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_water_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_water_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(6) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[6] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_temperature_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_temperature_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(7) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[7] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_weight_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_weight_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(8) ?: 0.17f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[8] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_melee_damage_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_melee_damage_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(9) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[9] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_movement_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_movement_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(10) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[10] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_fortitude_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_fortitude_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamed?.get(11) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamed = it.shooterGameMode.perLevelStatsMultiplierDinoTamed?.toMutableMap()?.apply { this[11] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_crafting_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_crafting_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamed != null
                )

                CheckboxSectionHeader(
                    checked = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null,
                    onCheckedChange = { newVal ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = if (newVal) ShooterGameMode.createPerLevelStatsMultiplierDinoTamedAddDefault() else null))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stats_tamed_add_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stats_tamed_add_hint)
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(0) ?: 0.14f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[0] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_health_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_health_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(1) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[1] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stamina_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stamina_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(2) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[2] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_torpidity_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_torpidity_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(3) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[3] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_oxygen_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_oxygen_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(4) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[4] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_food_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_food_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(5) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[5] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_water_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_water_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(6) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[6] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_temperature_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_temperature_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(7) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[7] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_weight_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_weight_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(8) ?: 0.14f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[8] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_melee_damage_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_melee_damage_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(9) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[9] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_movement_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_movement_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(10) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[10] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_fortitude_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_fortitude_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.get(11) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAdd = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd?.toMutableMap()?.apply { this[11] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_crafting_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_crafting_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAdd != null
                )

                CheckboxSectionHeader(
                    checked = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null,
                    onCheckedChange = { newVal ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = if (newVal) ShooterGameMode.createPerLevelStatsMultiplierDinoTamedAffinityDefault() else null))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stats_tamed_affinity_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stats_tamed_affinity_hint)
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(0) ?: 0.44f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[0] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_health_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_health_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(1) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[1] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stamina_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stamina_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(2) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[2] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_torpidity_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_torpidity_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(3) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[3] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_oxygen_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_oxygen_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(4) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[4] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_food_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_food_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(5) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[5] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_water_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_water_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(6) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[6] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_temperature_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_temperature_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(7) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[7] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_weight_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_weight_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(8) ?: 0.44f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[8] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_melee_damage_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_melee_damage_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(9) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[9] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_movement_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_movement_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(10) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[10] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_fortitude_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_fortitude_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.get(11) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoTamedAffinity = it.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity?.toMutableMap()?.apply { this[11] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_crafting_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_crafting_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoTamedAffinity != null
                )

                CheckboxSectionHeader(
                    checked = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null,
                    onCheckedChange = { newVal ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = if (newVal) ShooterGameMode.createPerLevelStatsMultiplierDinoWildDefault() else null))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stats_wild_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stats_wild_hint)
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(0) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[0] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_health_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_health_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(1) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[1] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_stamina_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_stamina_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(2) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[2] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_torpidity_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_torpidity_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(3) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[3] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_oxygen_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_oxygen_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(4) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[4] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_food_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_food_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(5) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[5] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_water_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_water_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(6) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[6] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_temperature_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_temperature_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(7) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[7] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_weight_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_weight_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(8) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[8] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_melee_damage_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_melee_damage_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(9) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[9] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_movement_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_movement_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(10) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[10] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_fortitude_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_fortitude_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.perLevelStatsMultiplierDinoWild?.get(11) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(perLevelStatsMultiplierDinoWild = it.shooterGameMode.perLevelStatsMultiplierDinoWild?.toMutableMap()?.apply { this[11] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_dino_per_level_crafting_speed_label),
                    hint = stringResource(Res.string.server_details_dino_per_level_crafting_speed_hint),
                    valueRange = 0.01f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.perLevelStatsMultiplierDinoWild != null
                )
            }
        }
    }
}
