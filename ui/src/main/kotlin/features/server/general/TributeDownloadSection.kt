package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource

@Composable
fun TributeDownloadSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val enableTributeDownloadsLabel = stringResource(Res.string.server_details_enable_tribute_downloads_label)
    val enableTributeDownloadsHint = stringResource(Res.string.server_details_enable_tribute_downloads_hint)
    val noSurvivorDownloadsLabel = stringResource(Res.string.server_details_no_survivor_downloads_label)
    val noSurvivorDownloadsHint = stringResource(Res.string.server_details_no_survivor_downloads_hint)
    val noItemDownloadsLabel = stringResource(Res.string.server_details_no_item_downloads_label)
    val noItemDownloadsHint = stringResource(Res.string.server_details_no_item_downloads_hint)
    val noDinosaurDownloadsLabel = stringResource(Res.string.server_details_no_dinosaur_downloads_label)
    val noDinosaurDownloadsHint = stringResource(Res.string.server_details_no_dinosaur_downloads_hint)
    val foreignDinoDownloadsLabel = stringResource(Res.string.server_details_foreign_dino_downloads_label)
    val foreignDinoDownloadsHint = stringResource(Res.string.server_details_foreign_dino_downloads_hint)

    model.server?.run {
        CheckboxSectionHeader(
            !gameUserSettings.serverSettings.noTributeDownloads,
            onCheckedChange = { newValue: Boolean ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(noTributeDownloads = newValue.not()))
                }
            },
            label = enableTributeDownloadsLabel,
            hint = enableTributeDownloadsHint,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FormCheckboxField(
                gameUserSettings.serverSettings.preventDownloadSurvivors,
                onCheckedChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(preventDownloadSurvivors = newValue))
                    }
                },
                label = noSurvivorDownloadsLabel,
                hint = noSurvivorDownloadsHint,
                enabled = !gameUserSettings.serverSettings.noTributeDownloads
            )
            FormCheckboxField(
                gameUserSettings.serverSettings.preventDownloadItems,
                onCheckedChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(preventDownloadItems = newValue))
                    }
                },
                label = noItemDownloadsLabel,
                hint = noItemDownloadsHint,
                enabled = !gameUserSettings.serverSettings.noTributeDownloads
            )
            FormCheckboxField(
                gameUserSettings.serverSettings.preventDownloadDinos,
                onCheckedChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(preventDownloadDinos = newValue))
                    }
                },
                label = noDinosaurDownloadsLabel,
                hint = noDinosaurDownloadsHint,
                enabled = !gameUserSettings.serverSettings.noTributeDownloads
            )
        }
        FormCheckboxField(
            gameUserSettings.serverSettings.crossArkAllowForeignDinoDownloads,
            onCheckedChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(crossArkAllowForeignDinoDownloads = newValue))
                }
            },
            label = foreignDinoDownloadsLabel,
            hint = foreignDinoDownloadsHint,
            enabled = !gameUserSettings.serverSettings.noTributeDownloads
        )
    }
}
