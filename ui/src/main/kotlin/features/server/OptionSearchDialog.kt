package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import arkascendedservermanager.ui.generated.resources.*
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.search.OptionSearchEntry
import eu.wynq.arkascendedservermanager.ui.features.server.search.optionSearchEntries
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedDialog
import io.github.kdroidfilter.nucleus.window.jewel.JewelDialogTitleBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun OptionSearchDialog(
    onSelect: (OptionSearchEntry) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val tabLabels = ServerDetailsTab.entries.associateWith { tab ->
        when (tab) {
            ServerDetailsTab.INFO -> stringResource(Res.string.server_details_info_tab)
            ServerDetailsTab.GENERAL -> stringResource(Res.string.server_details_general_tab)
            ServerDetailsTab.PROFILE -> stringResource(Res.string.server_details_profile_tab)
            ServerDetailsTab.MANAGEMENT -> stringResource(Res.string.server_details_management_tab)
            ServerDetailsTab.ENVIRONMENT -> stringResource(Res.string.server_details_environment_tab)
            ServerDetailsTab.RULES -> stringResource(Res.string.server_details_rules_tab)
            ServerDetailsTab.PLAYER -> stringResource(Res.string.server_details_player_tab)
            ServerDetailsTab.DINO_SETTINGS -> stringResource(Res.string.server_details_dino_settings_tab)
            ServerDetailsTab.TABLES -> stringResource(Res.string.server_details_tables_tab)
            ServerDetailsTab.STRUCTURES_AND_ITEMS -> stringResource(Res.string.server_details_structures_and_items_tab)
            ServerDetailsTab.ADMIN_AND_SECURITY -> stringResource(Res.string.server_details_admin_and_security_tab)
            ServerDetailsTab.ADVANCED_AND_LAUNCH -> stringResource(Res.string.server_details_advanced_and_launch_tab)
            ServerDetailsTab.CHAT_AND_HUD -> stringResource(Res.string.server_details_chat_and_hud_tab)
            ServerDetailsTab.CONFIG_EDITOR -> "Config Editor"
        }
    }
    val filtered = remember(query, tabLabels) {
        val tokens = query.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
        optionSearchEntries.filter { entry ->
            tokens.all { token ->
                entry.label.contains(token, ignoreCase = true) ||
                        entry.hint?.contains(token, ignoreCase = true) == true ||
                        tabLabels[entry.tab]?.contains(token, ignoreCase = true) == true
            }
        }.take(100)
    }
    val dialogState = rememberDialogState(size = DpSize(760.dp, 620.dp))

    JewelDecoratedDialog(
        onCloseRequest = onDismiss,
        title = stringResource(Res.string.server_details_find_settings_title),
        resizable = true,
        state = dialogState,
    ) {
        Column(Modifier.fillMaxSize().background(JewelTheme.globalColors.toolwindowBackground)) {
            JewelDialogTitleBar {
                Text(stringResource(Res.string.server_details_find_settings_title))
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FormTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = stringResource(Res.string.server_details_find_settings_search_label),
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    stringResource(Res.string.server_details_find_settings_count, filtered.size, optionSearchEntries.size),
                    style = JewelTheme.typography.labelTextStyle,
                )
                if (filtered.isEmpty()) {
                    Text(
                        stringResource(Res.string.server_details_find_settings_empty),
                        style = JewelTheme.typography.labelTextStyle,
                    )
                } else {
                    LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                        items(filtered, key = { "${it.tab}:${it.label}" }) { entry ->
                            val interaction = remember { MutableInteractionSource() }
                            val hovered by interaction.collectIsHoveredAsState()
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (hovered) JewelTheme.globalColors.panelBackground else Color.Transparent,
                                    )
                                    .hoverable(interaction)
                                    .clickable(interactionSource = interaction, indication = null) {
                                        onSelect(entry)
                                    }
                                    .padding(vertical = 6.dp, horizontal = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(entry.label)
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        tabLabels[entry.tab] ?: entry.tab.name,
                                        style = JewelTheme.typography.labelTextStyle,
                                    )
                                }
                                entry.hint?.let {
                                    Text(it, style = JewelTheme.typography.labelTextStyle)
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    DefaultButton(onClick = onDismiss) {
                        Text(stringResource(Res.string.action_cancel))
                    }
                }
            }
        }
    }
}
