package eu.wynq.arkascendedservermanager.ui.components

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
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_details_tables_catalog_cancel
import arkascendedservermanager.ui.generated.resources.server_details_tables_catalog_custom
import arkascendedservermanager.ui.generated.resources.server_details_tables_catalog_search
import arkascendedservermanager.ui.generated.resources.server_details_tables_catalog_title
import arkascendedservermanager.ui.generated.resources.server_details_tables_catalog_unverified
import arkascendedservermanager.ui.generated.resources.server_details_tables_catalog_button
import eu.wynq.arkascendedservermanager.core.catalog.ArkCatalogs
import eu.wynq.arkascendedservermanager.core.catalog.CatalogEntry
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedDialog
import io.github.kdroidfilter.nucleus.window.jewel.JewelDialogTitleBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

/**
 * A free-text class-name field with a catalog search button.  The text field is
 * always editable, so modded/custom class names are never blocked by the
 * catalog.
 */
@Composable
fun ClassNameCell(
    value: String,
    onValueChange: (String) -> Unit,
    entries: List<CatalogEntry>,
    modifier: Modifier = Modifier,
    entryValue: (CatalogEntry) -> String = { it.className },
    title: String = stringResource(Res.string.server_details_tables_catalog_title),
) {
    var dialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FormTextField(
            value = value,
            onValueChange = onValueChange,
            label = null,
            modifier = Modifier.weight(1f),
        )
        OutlinedButton(onClick = { dialogOpen = true }) {
            Text(stringResource(Res.string.server_details_tables_catalog_button))
        }
    }

    if (dialogOpen) {
        ClassPickerDialog(
            title = title,
            entries = entries,
            onSelect = { entry ->
                onValueChange(entryValue(entry))
                dialogOpen = false
            },
            onDismiss = { dialogOpen = false },
        )
    }
}

@Composable
fun ClassPickerDialog(
    title: String,
    entries: List<CatalogEntry>,
    onSelect: (CatalogEntry) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, entries) { ArkCatalogs.search(entries, query) }
    val dialogState = rememberDialogState(size = DpSize(760.dp, 600.dp))
    val searchInteraction = remember { MutableInteractionSource() }
    val searchHovered by searchInteraction.collectIsHoveredAsState()

    JewelDecoratedDialog(
        onCloseRequest = onDismiss,
        title = title,
        resizable = true,
        state = dialogState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(JewelTheme.globalColors.toolwindowBackground),
        ) {
            JewelDialogTitleBar { Text(title) }
            Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (searchHovered) JewelTheme.globalColors.panelBackground else Color.Transparent,
                        )
                        .hoverable(searchInteraction)
                        .padding(2.dp),
                ) {
                    FormTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = stringResource(Res.string.server_details_tables_catalog_search),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Text(
                    "${filtered.size} / ${entries.size}",
                    style = JewelTheme.typography.labelTextStyle,
                )
                LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                    items(filtered, key = { it.className }) { entry ->
                        val rowInteraction = remember { MutableInteractionSource() }
                        val hovered by rowInteraction.collectIsHoveredAsState()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (hovered) JewelTheme.globalColors.panelBackground else Color.Transparent,
                                )
                                .hoverable(rowInteraction)
                                .clickable(interactionSource = rowInteraction, indication = null) { onSelect(entry) }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(entry.name)
                                if (!entry.isAsaVerified) {
                                    Text(
                                        stringResource(Res.string.server_details_tables_catalog_unverified),
                                        style = JewelTheme.typography.labelTextStyle,
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                entry.category?.let {
                                    Text(it, style = JewelTheme.typography.labelTextStyle)
                                }
                            }
                            Text(entry.className, style = JewelTheme.typography.labelTextStyle)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick = {
                            if (query.isNotBlank()) {
                                onSelect(
                                    CatalogEntry(
                                        name = query,
                                        className = query.trim(),
                                        category = null,
                                    )
                                )
                            }
                        },
                        enabled = query.isNotBlank(),
                    ) {
                        Text(stringResource(Res.string.server_details_tables_catalog_custom))
                    }
                    Spacer(Modifier.weight(1f))
                    DefaultButton(onClick = onDismiss) {
                        Text(stringResource(Res.string.server_details_tables_catalog_cancel))
                    }
                }
            }
        }
    }
}

