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
            }
        }
    }
}