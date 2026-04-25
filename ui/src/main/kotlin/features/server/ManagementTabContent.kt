package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ManagementTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value


    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            model.server?.run {
                GroupHeader(stringResource(Res.string.server_details_automatic_management_group))
                FormCheckboxField(
                    settings.administration.restartAfterCrash,
                    onCheckedChange = { newValue ->
                        component.updateServer {
                            it.copy(
                                settings = it.settings.copy(
                                    administration = it.settings.administration.copy(
                                        restartAfterCrash = newValue
                                    )
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_restart_after_crash_label),
                    hint = stringResource(Res.string.server_details_restart_after_crash_hint)
                )
            }
        }
    }
}

