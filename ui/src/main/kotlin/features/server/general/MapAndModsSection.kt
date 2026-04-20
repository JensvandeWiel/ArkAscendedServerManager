package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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
            value = settings.administration.mods.joinToString(","),
            onValueChange = { newValue ->
                val mods = newValue.split(",").mapNotNull { rawValue -> rawValue.trim().takeIf { it.isNotEmpty() } }
                component.updateServerAdministrationSettings {
                    it.copy(mods = mods)
                }
            },
            label = modsLabel,
            hint = modsHint,
            error = !settings.administration.validateMods(),
        )
    }
}

