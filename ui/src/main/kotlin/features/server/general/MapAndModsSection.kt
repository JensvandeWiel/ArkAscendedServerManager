package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormFloatSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun MapAndModsSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val mapAndModsGroupLabel = stringResource(Res.string.server_details_group_map_and_mods)
    val mapLabel = stringResource(Res.string.server_details_map_label)
    val mapHint = stringResource(Res.string.server_details_map_hint)
    val modsLabel = stringResource(Res.string.server_details_mods_label)
    val modsHint = stringResource(Res.string.server_details_mods_hint)
    val difficultyLabel = stringResource(Res.string.server_details_difficulty_offset_label)
    val difficultyHint = stringResource(Res.string.server_details_difficulty_offset_hint)

    model.server?.run {
        GroupHeader(mapAndModsGroupLabel)
        FormTextField(
            value = settings.administration.map,
            onValueChange = { newValue ->
                component.updateServerAdministrationSettings {
                    it.copy(map = newValue)
                }
            },
            label = mapLabel,
            hint = mapHint,
            error = !settings.administration.validateMap(),
        )
        FormTextField(
            value = gameUserSettings.serverSettings.activeMods.joinToString(","),
            onValueChange = { newValue ->
                val mods = newValue.split(",")
                    .mapNotNull { it.trim().toIntOrNull() }
                    .filter { it > 0 }
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(activeMods = mods))
                }
            },
            label = modsLabel,
            hint = modsHint,
            error = !gameUserSettings.serverSettings.validateActiveMods(),
        )
        FormFloatSliderField(
            value = gameUserSettings.serverSettings.difficultyOffset,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(difficultyOffset = newValue))
                }
            },
            label = difficultyLabel,
            hint = difficultyHint,
            valueRange = 0.01f..10.0f,
            allowOutsideRange = true,
            showManualInput = true,
        )
    }
}
