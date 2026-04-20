@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.theme.defaultTabStyle
import org.jetbrains.jewel.ui.typography
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ServerScreen(component: ServerComponent) {
    val title = stringResource(Res.string.server_details_title)
    val titleSeparator = stringResource(Res.string.server_details_title_separator)
    val notFoundLabel = stringResource(Res.string.server_details_not_found)
    val errorLabel = stringResource(Res.string.server_details_error)
    val infoTabLabel = stringResource(Res.string.server_details_info_tab)
    val generalTabLabel = stringResource(Res.string.server_details_general_tab)
    val saveServerLabel = stringResource(Res.string.action_save_server)
    val deleteServerLabel = stringResource(Res.string.action_delete_server)
    val deleteDialogTitleFormat = stringResource(Res.string.server_details_delete_dialog_title_format)
    val deleteDialogMessage = stringResource(Res.string.server_details_delete_dialog_message)
    val deleteDialogDefaultTarget = stringResource(Res.string.server_details_delete_dialog_target_default)
    val model by component.model.subscribeAsState()
    val error by component.error.collectAsState()
    val selectedTab by component.selectedTab.subscribeAsState()
    val tabs =
        remember(selectedTab, infoTabLabel, generalTabLabel) {
            listOf(
                TabData.Default(
                    selected = selectedTab == ServerDetailsTab.INFO,
                    content = { tabState ->
                        SimpleTabContent(label = infoTabLabel, state = tabState)
                    },
                    onClick = component::selectInfoTab,
                    closable = false,
                ),
                TabData.Default(
                    selected = selectedTab == ServerDetailsTab.GENERAL,
                    content = { tabState ->
                        SimpleTabContent(label = generalTabLabel, state = tabState)
                    },
                    onClick = component::selectGeneralTab,
                    closable = false,
                ),
            )
        }

    if (model.deleteDialogOpen) {
        ConfirmationDialog(
            onConfirm = component::onDeletionDialogConfirmed,
            onDismiss = component::onDeletionDialogDismissed,
            deleteDialogTitleFormat.format(model.initialServer?.profileName ?: deleteDialogDefaultTarget),
            deleteDialogMessage,
        )
    }


    Column {
        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, style = JewelTheme.typography.h2TextStyle)
            Text(titleSeparator, style = JewelTheme.typography.h2TextStyle)
            Text(model.initialServer?.profileName ?: "", style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::saveServer, enabled = model.canSave()) {
                Text(saveServerLabel)
            }
            DefaultButton(onClick = component::openDeleteDialog) {
                Text(deleteServerLabel)
            }
        }
        TabStrip(
            tabs = tabs,
            style = JewelTheme.defaultTabStyle,
            modifier = Modifier.fillMaxWidth(),
        )
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            val loadedServer = model.initialServer
            if (loadedServer != null) {
                when (selectedTab) {
                    ServerDetailsTab.INFO -> {
                        InfoTabContent(component)
                    }

                    ServerDetailsTab.GENERAL -> {
                        GeneralTabContent(component)
                    }
                }
            } else if (error != null) {
                Text(stringResource(Res.string.server_details_error_format, errorLabel, error ?: ""))
            } else {
                Text(notFoundLabel)
            }
        }
    }
}

