@file:OptIn(ExperimentalFoundationApi::class)
package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.catalog.ArkCatalogs
import eu.wynq.arkascendedservermanager.core.catalog.CatalogEntry
import eu.wynq.arkascendedservermanager.core.ini.tables.ClassMultiplier
import eu.wynq.arkascendedservermanager.core.ini.tables.DinoSpawnWeight
import eu.wynq.arkascendedservermanager.core.ini.tables.EngramAutoUnlock
import eu.wynq.arkascendedservermanager.core.ini.tables.EngramOverride
import eu.wynq.arkascendedservermanager.core.ini.tables.NpcReplacement
import eu.wynq.arkascendedservermanager.core.ini.tables.StackOverride
import eu.wynq.arkascendedservermanager.core.ini.tables.TeleportEntry
import eu.wynq.arkascendedservermanager.ui.components.ClassNameCell
import eu.wynq.arkascendedservermanager.ui.components.ClassPickerDialog
import eu.wynq.arkascendedservermanager.ui.components.DataTable
import eu.wynq.arkascendedservermanager.ui.components.DataTableColumn
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.IconActionButton
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography

@Composable
fun TablesTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_tables_tab))
                Text(stringResource(Res.string.server_details_tables_intro))

                GroupHeader(stringResource(Res.string.server_details_tables_verified_header))
                ClassMultiplierTable(
                    title = stringResource(Res.string.server_details_tables_harvest_resource_label),
                    hint = stringResource(Res.string.server_details_tables_harvest_resource_hint),
                    entries = ArkCatalogs.resources,
                    rows = game.shooterGameMode.harvestResourceItemAmountClassMultipliers,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(harvestResourceItemAmountClassMultipliers = rows))
                        }
                    },
                )
                EngramOverrideTable(
                    rows = game.shooterGameMode.overrideNamedEngramEntries,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(overrideNamedEngramEntries = rows))
                        }
                    },
                )
                StringListTable(
                    title = stringResource(Res.string.server_details_tables_prevent_breeding_label),
                    hint = stringResource(Res.string.server_details_tables_prevent_breeding_hint),
                    entries = ArkCatalogs.creatures,
                    rows = game.shooterGameMode.preventBreedingForClassNames,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(preventBreedingForClassNames = rows))
                        }
                    },
                )
                TeleportTable(
                    rows = game.shooterGameMode.cheatTeleportLocations,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(cheatTeleportLocations = rows))
                        }
                    },
                )

                GroupHeader(stringResource(Res.string.server_details_tables_experimental_header))

                ClassMultiplierTable(
                    title = stringResource(Res.string.server_details_tables_wild_damage_label),
                    hint = stringResource(Res.string.server_details_tables_class_multiplier_hint),
                    entries = ArkCatalogs.creatures,
                    rows = game.shooterGameMode.dinoClassDamageMultipliers,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(dinoClassDamageMultipliers = rows))
                        }
                    },
                )
                ClassMultiplierTable(
                    title = stringResource(Res.string.server_details_tables_wild_resistance_label),
                    hint = stringResource(Res.string.server_details_tables_class_multiplier_hint),
                    entries = ArkCatalogs.creatures,
                    rows = game.shooterGameMode.dinoClassResistanceMultipliers,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(dinoClassResistanceMultipliers = rows))
                        }
                    },
                )
                ClassMultiplierTable(
                    title = stringResource(Res.string.server_details_tables_tamed_damage_label),
                    hint = stringResource(Res.string.server_details_tables_class_multiplier_hint),
                    entries = ArkCatalogs.creatures,
                    rows = game.shooterGameMode.tamedDinoClassDamageMultipliers,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(tamedDinoClassDamageMultipliers = rows))
                        }
                    },
                )
                ClassMultiplierTable(
                    title = stringResource(Res.string.server_details_tables_tamed_resistance_label),
                    hint = stringResource(Res.string.server_details_tables_class_multiplier_hint),
                    entries = ArkCatalogs.creatures,
                    rows = game.shooterGameMode.tamedDinoClassResistanceMultipliers,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(tamedDinoClassResistanceMultipliers = rows))
                        }
                    },
                )
                DinoSpawnWeightTable(
                    rows = game.shooterGameMode.dinoSpawnWeightMultipliers,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(dinoSpawnWeightMultipliers = rows))
                        }
                    },
                )
                NpcReplacementTable(
                    rows = game.shooterGameMode.npcReplacements,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(npcReplacements = rows))
                        }
                    },
                )
                StringListTable(
                    title = stringResource(Res.string.server_details_tables_prevent_tame_label),
                    hint = stringResource(Res.string.server_details_tables_prevent_tame_hint),
                    entries = ArkCatalogs.creatures,
                    rows = game.shooterGameMode.preventDinoTameClassNames,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(preventDinoTameClassNames = rows))
                        }
                    },
                )
                StackOverrideTable(
                    rows = game.shooterGameMode.configOverrideItemMaxQuantity,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(configOverrideItemMaxQuantity = rows))
                        }
                    },
                )
                EngramAutoUnlockTable(
                    rows = game.shooterGameMode.engramEntryAutoUnlocks,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(engramEntryAutoUnlocks = rows))
                        }
                    },
                )
                IntListTable(
                    rows = game.shooterGameMode.overridePlayerLevelEngramPoints,
                    onChange = { rows ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(overridePlayerLevelEngramPoints = rows))
                        }
                    },
                )
                ItemStatClampsTable(
                    clamps = game.shooterGameMode.itemStatClamps,
                    onChange = { clamps ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(itemStatClamps = clamps))
                        }
                    },
                )
                MutagenBoostTable(
                    title = stringResource(Res.string.server_details_tables_mutagen_label),
                    hint = stringResource(Res.string.server_details_tables_mutagen_hint),
                    values = game.shooterGameMode.mutagenLevelBoost,
                    onChange = { values ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(mutagenLevelBoost = values))
                        }
                    },
                )
                MutagenBoostTable(
                    title = stringResource(Res.string.server_details_tables_mutagen_bred_label),
                    hint = stringResource(Res.string.server_details_tables_mutagen_bred_hint),
                    values = game.shooterGameMode.mutagenLevelBoostBred,
                    onChange = { values ->
                        component.updateServerGame {
                            it.copy(shooterGameMode = it.shooterGameMode.copy(mutagenLevelBoostBred = values))
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ClassMultiplierTable(
    title: String,
    hint: String,
    entries: List<CatalogEntry>,
    rows: List<ClassMultiplier>,
    onChange: (List<ClassMultiplier>) -> Unit,
) {
    GroupHeader(title)
    val columns = listOf(
        DataTableColumn<ClassMultiplier>(stringResource(Res.string.server_details_tables_col_class), 3f) { index, row ->
            ClassNameCell(
                value = row.className,
                onValueChange = { onChange(rows.replacing(index, row.copy(className = it))) },
                entries = entries,
            )
        },
        DataTableColumn<ClassMultiplier>(stringResource(Res.string.server_details_tables_col_multiplier), 1.4f) { index, row ->
            TableNumberField(row.multiplier) {
                onChange(rows.replacing(index, row.copy(multiplier = it)))
            }
        },
        DataTableColumn<ClassMultiplier>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = hint,
        onAddDefault = { onChange(rows + ClassMultiplier()) },
        onAddFromCatalog = { entry -> onChange(rows + ClassMultiplier(className = entry.className)) },
    )
}

@Composable
private fun DinoSpawnWeightTable(
    rows: List<DinoSpawnWeight>,
    onChange: (List<DinoSpawnWeight>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_dino_spawns_label))
    val entries = ArkCatalogs.creatures
    val columns = listOf(
        DataTableColumn<DinoSpawnWeight>(stringResource(Res.string.server_details_tables_col_name_tag), 2.4f) { index, row ->
            ClassNameCell(
                value = row.dinoNameTag,
                onValueChange = { onChange(rows.replacing(index, row.copy(dinoNameTag = it))) },
                entries = entries,
                entryValue = { it.nameTag ?: it.className },
            )
        },
        DataTableColumn<DinoSpawnWeight>(stringResource(Res.string.server_details_tables_col_weight), 1.2f) { index, row ->
            TableNumberField(row.spawnWeightMultiplier) {
                onChange(rows.replacing(index, row.copy(spawnWeightMultiplier = it)))
            }
        },
        DataTableColumn<DinoSpawnWeight>(stringResource(Res.string.server_details_tables_col_override), 1f) { index, row ->
            TableCheckbox(row.overrideSpawnLimitPercentage) {
                onChange(rows.replacing(index, row.copy(overrideSpawnLimitPercentage = it)))
            }
        },
        DataTableColumn<DinoSpawnWeight>(stringResource(Res.string.server_details_tables_col_limit), 1.2f) { index, row ->
            TableNumberField(row.spawnLimitPercentage) {
                onChange(rows.replacing(index, row.copy(spawnLimitPercentage = it)))
            }
        },
        DataTableColumn<DinoSpawnWeight>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = stringResource(Res.string.server_details_tables_dino_spawns_hint),
        onAddDefault = { onChange(rows + DinoSpawnWeight()) },
        onAddFromCatalog = { entry ->
            onChange(rows + DinoSpawnWeight(dinoNameTag = entry.nameTag ?: entry.className))
        },
    )
}

@Composable
private fun NpcReplacementTable(
    rows: List<NpcReplacement>,
    onChange: (List<NpcReplacement>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_npc_replacements_label))
    val entries = ArkCatalogs.creatures
    val columns = listOf(
        DataTableColumn<NpcReplacement>(stringResource(Res.string.server_details_tables_col_from), 2.5f) { index, row ->
            ClassNameCell(
                value = row.fromClassName,
                onValueChange = { onChange(rows.replacing(index, row.copy(fromClassName = it))) },
                entries = entries,
            )
        },
        DataTableColumn<NpcReplacement>(stringResource(Res.string.server_details_tables_col_to), 2.5f) { index, row ->
            ClassNameCell(
                value = row.toClassName,
                onValueChange = { onChange(rows.replacing(index, row.copy(toClassName = it))) },
                entries = entries,
            )
        },
        DataTableColumn<NpcReplacement>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = stringResource(Res.string.server_details_tables_npc_replacements_hint),
        onAddDefault = { onChange(rows + NpcReplacement()) },
        onAddFromCatalog = { entry -> onChange(rows + NpcReplacement(fromClassName = entry.className)) },
    )
}

@Composable
private fun EngramOverrideTable(
    rows: List<EngramOverride>,
    onChange: (List<EngramOverride>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_engram_overrides_label))
    val entries = ArkCatalogs.engrams
    val columns = listOf(
        DataTableColumn<EngramOverride>(stringResource(Res.string.server_details_tables_col_engram), 3f) { index, row ->
            ClassNameCell(
                value = row.engramClassName,
                onValueChange = { onChange(rows.replacing(index, row.copy(engramClassName = it))) },
                entries = entries,
            )
        },
        DataTableColumn<EngramOverride>(stringResource(Res.string.server_details_tables_col_hidden), 0.8f) { index, row ->
            TableCheckbox(row.engramHidden) {
                onChange(rows.replacing(index, row.copy(engramHidden = it)))
            }
        },
        DataTableColumn<EngramOverride>(stringResource(Res.string.server_details_tables_col_points), 1f) { index, row ->
            TableIntField(row.engramPointsCost) {
                onChange(rows.replacing(index, row.copy(engramPointsCost = it)))
            }
        },
        DataTableColumn<EngramOverride>(stringResource(Res.string.server_details_tables_col_level), 1f) { index, row ->
            TableIntField(row.engramLevelRequirement) {
                onChange(rows.replacing(index, row.copy(engramLevelRequirement = it)))
            }
        },
        DataTableColumn<EngramOverride>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = stringResource(Res.string.server_details_tables_engram_overrides_hint),
        onAddDefault = { onChange(rows + EngramOverride()) },
        onAddFromCatalog = { entry -> onChange(rows + EngramOverride(engramClassName = entry.className)) },
    )
}

@Composable
private fun EngramAutoUnlockTable(
    rows: List<EngramAutoUnlock>,
    onChange: (List<EngramAutoUnlock>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_engram_auto_unlocks_label))
    val entries = ArkCatalogs.engrams
    val columns = listOf(
        DataTableColumn<EngramAutoUnlock>(stringResource(Res.string.server_details_tables_col_engram), 3f) { index, row ->
            ClassNameCell(
                value = row.engramClassName,
                onValueChange = { onChange(rows.replacing(index, row.copy(engramClassName = it))) },
                entries = entries,
            )
        },
        DataTableColumn<EngramAutoUnlock>(stringResource(Res.string.server_details_tables_col_level), 1f) { index, row ->
            TableIntField(row.levelToAutoUnlock) {
                onChange(rows.replacing(index, row.copy(levelToAutoUnlock = it)))
            }
        },
        DataTableColumn<EngramAutoUnlock>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = stringResource(Res.string.server_details_tables_engram_auto_unlocks_hint),
        onAddDefault = { onChange(rows + EngramAutoUnlock()) },
        onAddFromCatalog = { entry -> onChange(rows + EngramAutoUnlock(engramClassName = entry.className)) },
    )
}

@Composable
private fun StackOverrideTable(
    rows: List<StackOverride>,
    onChange: (List<StackOverride>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_stack_overrides_label))
    val entries = ArkCatalogs.items
    val columns = listOf(
        DataTableColumn<StackOverride>(stringResource(Res.string.server_details_tables_col_item), 3f) { index, row ->
            ClassNameCell(
                value = row.itemClassString,
                onValueChange = { onChange(rows.replacing(index, row.copy(itemClassString = it))) },
                entries = entries,
            )
        },
        DataTableColumn<StackOverride>(stringResource(Res.string.server_details_tables_col_quantity), 1f) { index, row ->
            TableIntField(row.quantity.maxItemQuantity) {
                onChange(rows.replacing(index, row.copy(quantity = row.quantity.copy(maxItemQuantity = it))))
            }
        },
        DataTableColumn<StackOverride>(stringResource(Res.string.server_details_tables_col_ignore), 1f) { index, row ->
            TableCheckbox(row.quantity.ignoreMultiplier) {
                onChange(rows.replacing(index, row.copy(quantity = row.quantity.copy(ignoreMultiplier = it))))
            }
        },
        DataTableColumn<StackOverride>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = stringResource(Res.string.server_details_tables_stack_overrides_hint),
        onAddDefault = { onChange(rows + StackOverride()) },
        onAddFromCatalog = { entry -> onChange(rows + StackOverride(itemClassString = entry.className)) },
    )
}

@Composable
private fun TeleportTable(
    rows: List<TeleportEntry>,
    onChange: (List<TeleportEntry>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_teleports_label))
    val columns = listOf(
        DataTableColumn<TeleportEntry>(stringResource(Res.string.server_details_tables_col_name), 2f) { index, row ->
            FormTextField(
                value = row.teleportName,
                onValueChange = { onChange(rows.replacing(index, row.copy(teleportName = it))) },
                label = null,
            )
        },
        DataTableColumn<TeleportEntry>(stringResource(Res.string.server_details_tables_col_x), 1f) { index, row ->
            TableNumberField(row.teleportLocation.x) {
                onChange(rows.replacing(index, row.copy(teleportLocation = row.teleportLocation.copy(x = it))))
            }
        },
        DataTableColumn<TeleportEntry>(stringResource(Res.string.server_details_tables_col_y), 1f) { index, row ->
            TableNumberField(row.teleportLocation.y) {
                onChange(rows.replacing(index, row.copy(teleportLocation = row.teleportLocation.copy(y = it))))
            }
        },
        DataTableColumn<TeleportEntry>(stringResource(Res.string.server_details_tables_col_z), 1f) { index, row ->
            TableNumberField(row.teleportLocation.z) {
                onChange(rows.replacing(index, row.copy(teleportLocation = row.teleportLocation.copy(z = it))))
            }
        },
        DataTableColumn<TeleportEntry>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        DefaultButton(onClick = { onChange(rows + TeleportEntry()) }) {
            Text(stringResource(Res.string.server_details_tables_add_row))
        }
        InfoText(stringResource(Res.string.server_details_tables_teleports_hint))
    }
}

@Composable
private fun StringListTable(
    title: String,
    hint: String,
    entries: List<CatalogEntry>,
    rows: List<String>,
    onChange: (List<String>) -> Unit,
) {
    GroupHeader(title)
    val columns = listOf(
        DataTableColumn<String>(stringResource(Res.string.server_details_tables_col_class), 4f) { index, row ->
            ClassNameCell(
                value = row,
                onValueChange = { onChange(rows.replacing(index, it)) },
                entries = entries,
            )
        },
        DataTableColumn<String>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, rows, stringResource(Res.string.server_details_tables_empty))
    AddRowButtons(
        entries = entries,
        hint = hint,
        onAddDefault = { onChange(rows + "") },
        onAddFromCatalog = { entry -> onChange(rows + entry.className) },
    )
}

@Composable
private fun IntListTable(
    rows: List<Int>,
    onChange: (List<Int>) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_level_engram_points_label))
    val levelRows = rows.mapIndexed { index, points -> LevelRow(index, points) }
    val columns = listOf(
        DataTableColumn<LevelRow>(stringResource(Res.string.server_details_tables_col_level), 1f) { _, row ->
            Text("${row.index}")
        },
        DataTableColumn<LevelRow>(stringResource(Res.string.server_details_tables_col_points), 1.5f) { _, row ->
            TableIntField(row.points) { newValue ->
                onChange(rows.replacing(row.index, newValue))
            }
        },
        DataTableColumn<LevelRow>("", 0.5f) { index, _ ->
            RemoveButton { onChange(rows.removing(index)) }
        },
    )
    DataTable(columns, levelRows, stringResource(Res.string.server_details_tables_empty))
    Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        DefaultButton(onClick = { onChange(rows + 0) }) {
            Text(stringResource(Res.string.server_details_tables_add_row))
        }
        InfoText(stringResource(Res.string.server_details_tables_level_engram_points_hint))
    }
}

@Composable
private fun ItemStatClampsTable(
    clamps: Map<Int, Int>?,
    onChange: (Map<Int, Int>?) -> Unit,
) {
    GroupHeader(stringResource(Res.string.server_details_tables_item_stat_clamps_label))
    FormCheckboxField(
        checked = clamps != null,
        onCheckedChange = { enabled -> onChange(if (enabled) (0..7).associateWith { 0 } else null) },
        label = stringResource(Res.string.server_details_tables_enable_item_stat_clamps_label),
        hint = stringResource(Res.string.server_details_tables_item_stat_clamps_hint),
    )
    if (clamps != null) {
        val labels = listOf(
            stringResource(Res.string.server_details_tables_stat_clamp_generic_quality),
            stringResource(Res.string.server_details_tables_stat_clamp_armor),
            stringResource(Res.string.server_details_tables_stat_clamp_max_durability),
            stringResource(Res.string.server_details_tables_stat_clamp_weapon_damage),
            stringResource(Res.string.server_details_tables_stat_clamp_weapon_clip_ammo),
            stringResource(Res.string.server_details_tables_stat_clamp_hypothermal_insulation),
            stringResource(Res.string.server_details_tables_stat_clamp_weight),
            stringResource(Res.string.server_details_tables_stat_clamp_hyperthermal_insulation),
        )
        val statRows = labels.mapIndexed { index, label -> StatRow(index, label) }
        val columns = listOf(
            DataTableColumn<StatRow>(stringResource(Res.string.server_details_tables_col_stat), 3f) { _, row ->
                Text(row.label)
            },
            DataTableColumn<StatRow>(stringResource(Res.string.server_details_tables_col_value), 1.5f) { _, row ->
                TableIntField(clamps[row.key] ?: 0) { newValue -> onChange(clamps + (row.key to newValue)) }
            },
        )
        DataTable(columns, statRows, stringResource(Res.string.server_details_tables_empty))
    }
}

@Composable
private fun MutagenBoostTable(
    title: String,
    hint: String,
    values: Map<Int, Int>?,
    onChange: (Map<Int, Int>?) -> Unit,
) {
    GroupHeader(title)
    FormCheckboxField(
        checked = values != null,
        onCheckedChange = { enabled -> onChange(if (enabled) (0..11).associateWith { 0 } else null) },
        label = stringResource(Res.string.server_details_tables_enable_mutagen_label),
        hint = hint,
    )
    if (values != null) {
        val labels = listOf(
            stringResource(Res.string.server_details_player_per_level_health_label),
            stringResource(Res.string.server_details_player_per_level_stamina_label),
            stringResource(Res.string.server_details_player_per_level_torpidity_label),
            stringResource(Res.string.server_details_player_per_level_oxygen_label),
            stringResource(Res.string.server_details_player_per_level_food_label),
            stringResource(Res.string.server_details_player_per_level_water_label),
            stringResource(Res.string.server_details_player_per_level_temperature_label),
            stringResource(Res.string.server_details_player_per_level_weight_label),
            stringResource(Res.string.server_details_player_per_level_melee_damage_label),
            stringResource(Res.string.server_details_player_per_level_movement_speed_label),
            stringResource(Res.string.server_details_player_per_level_fortitude_label),
            stringResource(Res.string.server_details_player_per_level_crafting_speed_label),
        )
        val statRows = labels.mapIndexed { index, label -> StatRow(index, label) }
        val columns = listOf(
            DataTableColumn<StatRow>(stringResource(Res.string.server_details_tables_col_stat), 3f) { _, row ->
                Text(row.label)
            },
            DataTableColumn<StatRow>(stringResource(Res.string.server_details_tables_col_value), 1.5f) { _, row ->
                TableIntField(values[row.key] ?: 0) { newValue -> onChange(values + (row.key to newValue)) }
            },
        )
        DataTable(columns, statRows, stringResource(Res.string.server_details_tables_empty))
    }
}

private data class LevelRow(val index: Int, val points: Int)
private data class StatRow(val key: Int, val label: String)

@Composable
private fun TableNumberField(value: Float, onValueChange: (Float) -> Unit) {
    FormTextField(
        value = value.toString(),
        onValueChange = { text -> text.toFloatOrNull()?.let(onValueChange) },
        label = null,
    )
}

@Composable
private fun TableIntField(value: Int, onValueChange: (Int) -> Unit) {
    FormTextField(
        value = value.toString(),
        onValueChange = { text -> text.toIntOrNull()?.let(onValueChange) },
        label = null,
    )
}

@Composable
private fun TableCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    FormCheckboxField(
        checked = checked,
        onCheckedChange = onCheckedChange,
        label = "",
        smallCheckbox = true,
    )
}

@Composable
private fun RemoveButton(onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        IconActionButton(
            key = AllIconsKeys.General.Delete,
            contentDescription = stringResource(Res.string.server_details_tables_remove),
            onClick = onClick,
            focusable = false,
            tooltip = { Text(stringResource(Res.string.server_details_tables_remove)) },
            tooltipPlacement = TooltipPlacement.ComponentRect(
                Alignment.CenterStart,
                Alignment.CenterStart,
                offset = DpOffset((-6).dp, 0.dp),
            ),
        )
    }
}

@Composable
private fun AddRowButtons(
    entries: List<CatalogEntry>,
    hint: String,
    onAddDefault: () -> Unit,
    onAddFromCatalog: (CatalogEntry) -> Unit,
) {
    var pickerOpen by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DefaultButton(onClick = onAddDefault) {
                Text(stringResource(Res.string.server_details_tables_add_row))
            }
            OutlinedButton(onClick = { pickerOpen = true }) {
                Text(stringResource(Res.string.server_details_tables_add_from_catalog))
            }
        }
        InfoText(hint)
    }
    if (pickerOpen) {
        ClassPickerDialog(
            title = stringResource(Res.string.server_details_tables_catalog_title),
            entries = entries,
            onSelect = { entry ->
                onAddFromCatalog(entry)
                pickerOpen = false
            },
            onDismiss = { pickerOpen = false },
        )
    }
}

private fun <T> List<T>.replacing(index: Int, value: T): List<T> =
    toMutableList().also { it[index] = value }

private fun <T> List<T>.removing(index: Int): List<T> =
    toMutableList().also { it.removeAt(index) }


