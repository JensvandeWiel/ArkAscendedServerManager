package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import arkascendedservermanager.ui.generated.resources.*
import eu.wynq.arkascendedservermanager.core.ini.Game
import eu.wynq.arkascendedservermanager.core.ini.ShooterGameMode
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun PlayerTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_player_tab))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormCheckboxField(
                            gameUserSettings.serverSettings.allowFlyerCarryPvE,
                            onCheckedChange = { newval ->
                                component.updateServerGameUserSettings {
                                    it.copy(serverSettings = it.serverSettings.copy(allowFlyerCarryPvE = newval))
                                }
                            },
                            label = stringResource(Res.string.server_details_player_allow_flyer_carry_pve_label),
                            hint = stringResource(Res.string.server_details_player_allow_flyer_carry_pve_hint)
                        )
                    }
                }

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.xpMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(xpMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_xp_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_xp_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.playerDamageMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(playerDamageMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_damage_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_damage_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.playerResistanceMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(playerResistanceMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_resistance_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_resistance_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.playerCharacterWaterDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(playerCharacterWaterDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_water_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_water_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.playerCharacterFoodDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(playerCharacterFoodDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_food_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_food_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.playerCharacterStaminaDrainMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(playerCharacterStaminaDrainMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_stamina_drain_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_stamina_drain_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = gameUserSettings.serverSettings.playerCharacterHealthRecoveryMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGameUserSettings {
                            it.copy(serverSettings = it.serverSettings.copy(playerCharacterHealthRecoveryMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_health_recovery_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_health_recovery_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = game.shooterGameMode.playerHarvestingDamageMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerHarvestingDamageMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_harvesting_damage_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_harvesting_damage_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = game.shooterGameMode.craftingSkillBonusMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(craftingSkillBonusMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_crafting_skill_bonus_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_crafting_skill_bonus_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                FormFloatSliderField(
                    value = game.shooterGameMode.maxFallSpeedMultiplier,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(maxFallSpeedMultiplier = newval))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_max_fall_speed_multiplier_label),
                    hint = stringResource(Res.string.server_details_player_max_fall_speed_multiplier_hint),
                    valueRange = 0.1f..5.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                )

                CheckboxSectionHeader(
                    checked = game.shooterGameMode.playerBaseStatMultipliers != null,
                    onCheckedChange = { newVal ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = if (newVal) ShooterGameMode.createPlayerBaseStatMultipliersDefault() else null))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_multipliers_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_multipliers_hint)
                )

                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(0) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[0] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_health_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_health_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(1) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[1] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_stamina_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_stamina_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(2) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[2] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_torpidity_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_torpidity_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(3) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[3] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_oxygen_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_oxygen_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(4) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[4] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_food_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_food_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(5) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[5] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_water_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_water_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
                FormFloatSliderField(
                    value = game.shooterGameMode.playerBaseStatMultipliers?.get(7) ?: 1.0f,
                    onValueChange = { newval ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(playerBaseStatMultipliers = it.shooterGameMode.playerBaseStatMultipliers?.toMutableMap()?.apply { this[7] = newval }))
                        }
                    },
                    label = stringResource(Res.string.server_details_player_base_stat_weight_label),
                    hint = stringResource(Res.string.server_details_player_base_stat_weight_hint),
                    valueRange = 0.1f..25.0f,
                    showManualInput = true,
                    allowOutsideRange = true,
                    enabled = game.shooterGameMode.playerBaseStatMultipliers != null
                )
            }
        }
    }
}