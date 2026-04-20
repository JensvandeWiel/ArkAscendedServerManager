package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_details_auto_save_period_hint
import arkascendedservermanager.ui.generated.resources.server_details_auto_save_period_label
import arkascendedservermanager.ui.generated.resources.server_details_group_saves
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun SavesSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val savesGroupLabel = stringResource(Res.string.server_details_group_saves)
    val autoSavePeriodLabel = stringResource(Res.string.server_details_auto_save_period_label)
    val autoSavePeriodHint = stringResource(Res.string.server_details_auto_save_period_hint)

    model.server?.run {
        GroupHeader(savesGroupLabel)
        FormSliderField(
            value = gameUserSettings.serverSettings.autoSavePeriodMinutes,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(serverSettings = it.serverSettings.copy(autoSavePeriodMinutes = newValue))
                }
            },
            label = autoSavePeriodLabel,
            valueRange = 0..240,
            hint = autoSavePeriodHint,
            allowOutsideRange = true,
            showManualInput = true,
            error = !gameUserSettings.serverSettings.validateAutoSavePeriodMinutes(),
        )
    }
}

