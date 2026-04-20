package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormTextarea
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun MotdSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val motdGroupLabel = stringResource(Res.string.server_details_group_motd)
    val motdMessageLabel = stringResource(Res.string.server_details_motd_message_label)
    val motdMessageHint = stringResource(Res.string.server_details_motd_message_hint)
    val motdDurationLabel = stringResource(Res.string.server_details_motd_duration_label)
    val motdDurationHint = stringResource(Res.string.server_details_motd_duration_hint)

    model.server?.run {
        GroupHeader(motdGroupLabel)
        FormTextarea(
            value = gameUserSettings.messageOfTheDay.message,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(messageOfTheDay = it.messageOfTheDay.copy(message = newValue))
                }
            },
            label = motdMessageLabel,
            modifier = Modifier.fillMaxWidth().height(150.dp),
            hint = motdMessageHint,
            error = !gameUserSettings.messageOfTheDay.validate()
        )
        FormSliderField(
            value = gameUserSettings.messageOfTheDay.duration,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(messageOfTheDay = it.messageOfTheDay.copy(duration = newValue))
                }
            },
            label = motdDurationLabel,
            valueRange = 0..120,
            hint = motdDurationHint,
            allowOutsideRange = true,
            showManualInput = true,
            error = !gameUserSettings.messageOfTheDay.validate(),
        )
    }
}
