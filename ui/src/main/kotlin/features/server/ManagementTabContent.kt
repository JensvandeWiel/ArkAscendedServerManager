package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormNullableTextField
import eu.wynq.arkascendedservermanager.ui.helpers.getPowerStateLabel
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
                CheckboxSectionHeader(
                    settings.administration.sendServerUpdatesToDiscord,
                    onCheckedChange = { newValue ->
                        component.updateServer {
                            it.copy(
                                settings = it.settings.copy(
                                    administration = it.settings.administration.copy(
                                        sendServerUpdatesToDiscord = newValue
                                    )
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_discord_webhook_label),
                    hint = stringResource(Res.string.server_details_discord_webhook_hint),
                )
                FormNullableTextField(
                    value = settings.administration.sendServerUpdatesWebhookUrl,
                    onValueChange = { newValue ->
                        component.updateServer {
                            it.copy(
                                settings = it.settings.copy(
                                    administration = it.settings.administration.copy(
                                        sendServerUpdatesWebhookUrl = newValue
                                    )
                                )
                            )
                        }
                    },
                    label = stringResource(Res.string.server_details_webhook_url_label),
                    hint = stringResource(Res.string.server_details_webhook_url_hint),
                    enabled = settings.administration.sendServerUpdatesToDiscord,
                    error = !settings.administration.validateSendServerUpdatesWebhookUrl(),
                )
                PowerState.entries.filter { it != PowerState.Unknown }.chunked(2).forEach { rowStates ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowStates.forEach { powerState ->
                            FormCheckboxField(
                                checked = settings.administration.sendServerUpdatesEvents.contains(powerState),
                                onCheckedChange = { newValue ->
                                    component.updateServer {
                                        val currentEvents =
                                            it.settings.administration.sendServerUpdatesEvents.toMutableSet()
                                        if (newValue) {
                                            currentEvents.add(powerState)
                                        } else {
                                            currentEvents.remove(powerState)
                                        }
                                        it.copy(
                                            settings = it.settings.copy(
                                                administration = it.settings.administration.copy(
                                                    sendServerUpdatesEvents = currentEvents
                                                )
                                            )
                                        )
                                    }
                                },
                                label = stringResource(
                                    Res.string.server_details_send_power_state_update_label,
                                    getPowerStateLabel(powerState)
                                ),
                                hint = stringResource(
                                    Res.string.server_details_send_power_state_update_hint,
                                    getPowerStateLabel(powerState)
                                ),
                                enabled = settings.administration.sendServerUpdatesToDiscord,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowStates.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

