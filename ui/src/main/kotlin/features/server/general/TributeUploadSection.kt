package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun TributeUploadSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val tributeUploadOptionsGroupLabel = stringResource(Res.string.server_details_group_tribute_upload_options)
    val noSurvivorUploadsLabel = stringResource(Res.string.server_details_no_survivor_uploads_label)
    val noSurvivorUploadsHint = stringResource(Res.string.server_details_no_survivor_uploads_hint)
    val noItemUploadsLabel = stringResource(Res.string.server_details_no_item_uploads_label)
    val noItemUploadsHint = stringResource(Res.string.server_details_no_item_uploads_hint)
    val noDinosaurUploadsLabel = stringResource(Res.string.server_details_no_dinosaur_uploads_label)
    val noDinosaurUploadsHint = stringResource(Res.string.server_details_no_dinosaur_uploads_hint)

    model.server?.run {
        GroupHeader(tributeUploadOptionsGroupLabel)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FormCheckboxField(
                gameUserSettings.serverSettings.preventUploadSurvivors,
                onCheckedChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(preventUploadSurvivors = newValue))
                    }
                },
                label = noSurvivorUploadsLabel,
                hint = noSurvivorUploadsHint,
            )
            FormCheckboxField(
                gameUserSettings.serverSettings.preventUploadItems,
                onCheckedChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(preventUploadItems = newValue))
                    }
                },
                label = noItemUploadsLabel,
                hint = noItemUploadsHint,
            )
            FormCheckboxField(
                gameUserSettings.serverSettings.preventUploadDinos,
                onCheckedChange = { newValue ->
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(preventUploadDinos = newValue))
                    }
                },
                label = noDinosaurUploadsLabel,
                hint = noDinosaurUploadsHint,
            )
        }
    }
}
