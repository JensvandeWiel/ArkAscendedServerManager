package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import org.jetbrains.compose.resources.stringResource
import arkascendedservermanager.ui.generated.resources.*
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding

@Composable
fun EnvironmentTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_environment_multipliers))
                FormFloatSliderField(
                    gameUserSettings.serverSettings.tamingSpeedMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(tamingSpeedMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_taming_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_taming_speed_hint),
                )
                FormFloatSliderField(
                    gameUserSettings.serverSettings.harvestAmountMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(harvestAmountMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_harvest_amount_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_harvest_amount_hint),
                )
                FormFloatSliderField(
                    gameUserSettings.serverSettings.harvestHealthMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(harvestHealthMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_harvest_health_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_harvest_health_hint),
                )
                FormFloatSliderField(
                    gameUserSettings.serverSettings.resourcesRespawnPeriodMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(resourcesRespawnPeriodMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_resource_respawn_period_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_resource_respawn_period_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.resourceNoReplenishRadiusPlayers,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(resourceNoReplenishRadiusPlayers = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_suppress_replenishment_radius_players_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_suppress_replenishment_radius_players_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.resourceNoReplenishRadiusStructures,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(resourceNoReplenishRadiusStructures = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_suppress_replenishment_radius_structures_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_suppress_replenishment_radius_structures_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.baseTemperatureMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(baseTemperatureMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_base_temperature_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_base_temperature_hint),
                )
                FormFloatSliderField(
                    gameUserSettings.serverSettings.dayCycleSpeedScale,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(dayCycleSpeedScale = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_day_cycle_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_day_cycle_speed_hint)
                )
                FormFloatSliderField(
                    gameUserSettings.serverSettings.dayTimeSpeedScale,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(dayTimeSpeedScale = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_day_time_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_day_time_speed_hint)
                )
                FormFloatSliderField(
                    gameUserSettings.serverSettings.nightTimeSpeedScale,
                    onValueChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(nightTimeSpeedScale = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_night_time_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_night_time_speed_hint)
                )
                FormFloatSliderField(
                    game.shooterGameMode.globalSpoilingTimeMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(globalSpoilingTimeMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_global_spoiling_time_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_global_spoiling_time_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.globalItemDecompositionTimeMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(globalItemDecompositionTimeMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_global_item_decomposition_time_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_global_item_decomposition_time_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.globalCorpseDecompositionTimeMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(globalCorpseDecompositionTimeMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_global_corpse_decomposition_time_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_global_corpse_decomposition_time_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.cropDecaySpeedMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(cropDecaySpeedMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_crop_decay_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_crop_decay_speed_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.cropGrowthSpeedMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(cropGrowthSpeedMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_crop_growth_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_crop_growth_speed_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.layEggIntervalMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(layEggIntervalMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_lay_egg_interval_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_lay_egg_interval_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.poopIntervalMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(poopIntervalMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_poop_interval_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_poop_interval_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.hairGrowthSpeedMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(hairGrowthSpeedMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_hair_growth_speed_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_hair_growth_speed_hint),
                )
                GroupHeader(stringResource(Res.string.server_details_environment_earned_xp_multipliers))
                FormFloatSliderField(
                    game.shooterGameMode.genericXPMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(genericXPMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_generic_xp_multiplier_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_generic_xp_multiplier_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.craftXPMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(craftXPMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_craft_xp_multiplier_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_craft_xp_multiplier_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.harvestXPMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(harvestXPMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_harvest_xp_multiplier_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_harvest_xp_multiplier_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.killXPMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(killXPMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_kill_xp_multiplier_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_kill_xp_multiplier_hint),
                )
                FormFloatSliderField(
                    game.shooterGameMode.specialXPMultiplier,
                    onValueChange = { newValue ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(specialXPMultiplier = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_special_xp_multiplier_label),
                    valueRange = 0.0f..10.0f,
                    allowOutsideRange = true,
                    showManualInput = true,
                    hint = stringResource(Res.string.server_details_environment_special_xp_multiplier_hint),
                )



                GroupHeader(stringResource(Res.string.server_details_environment_options))
                FormCheckboxField(
                    gameUserSettings.serverSettings.clampResourceHarvestDamage,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(clampResourceHarvestDamage = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_clamp_resource_harvest_damage_label),
                    hint = stringResource(Res.string.server_details_environment_clamp_resource_harvest_damage_hint),
                )
                FormCheckboxField(
                    gameUserSettings.serverSettings.disableWeatherFog,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(disableWeatherFog = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_disable_weather_fog_label),
                    hint = stringResource(Res.string.server_details_environment_disable_weather_fog_hint),
                )
                FormCheckboxField(
                    gameUserSettings.serverSettings.clampItemSpoilingTimes,
                    onCheckedChange = { newValue ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(clampItemSpoilingTimes = newValue))
                        }
                    },
                    label = stringResource(Res.string.server_details_environment_clamp_item_spoiling_times_label),
                    hint = stringResource(Res.string.server_details_environment_clamp_item_spoiling_times_hint),
                )
            }
        }
    }
}
