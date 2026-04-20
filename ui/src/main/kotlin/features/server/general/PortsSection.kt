package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun PortsSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val portsGroupLabel = stringResource(Res.string.server_details_group_ports)
    val serverPortLabel = stringResource(Res.string.server_details_server_port_label)
    val serverPortHint = stringResource(Res.string.server_details_server_port_hint)
    val peerPortLabel = stringResource(Res.string.server_details_peer_port_label)
    val peerPortHint = stringResource(Res.string.server_details_peer_port_hint)
    val queryPortLabel = stringResource(Res.string.server_details_query_port_label)
    val queryPortHint = stringResource(Res.string.server_details_query_port_hint)
    val rconLabel = stringResource(Res.string.server_details_rcon_label)
    val rconHint = stringResource(Res.string.server_details_rcon_hint)
    val rconPortLabel = stringResource(Res.string.server_details_rcon_port_label)
    val rconPortHint = stringResource(Res.string.server_details_rcon_port_hint)
    val rconServerGameLogBufferLabel = stringResource(Res.string.server_details_rcon_server_game_log_buffer_label)
    val rconServerGameLogBufferHint = stringResource(Res.string.server_details_rcon_server_game_log_buffer_hint)

    model.server?.run {
        GroupHeader(portsGroupLabel)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormTextField(
                value = settings.administration.serverPort.toString(),
                onValueChange = { newValue ->
                    val port = newValue.toIntOrNull()
                    if (port != null) {
                        component.updateServerAdministrationSettings {
                            it.copy(serverPort = port)
                        }
                    }
                },
                label = serverPortLabel,
                modifier = Modifier.weight(1f),
                hint = serverPortHint,
                error = !settings.administration.validateServerPort(),
            )
            FormTextField(
                value = settings.administration.peerPort.toString(),
                onValueChange = { /* Peer port is always server port +1, so it is not editable. */ },
                label = peerPortLabel,
                modifier = Modifier.weight(1f),
                hint = peerPortHint,
                error = !settings.administration.validatePeerPort(),
                readOnly = true,
            )
            FormTextField(
                value = settings.administration.queryPort.toString(),
                onValueChange = { newValue ->
                    val port = newValue.toIntOrNull()
                    if (port != null) {
                        component.updateServerAdministrationSettings {
                            it.copy(queryPort = port)
                        }
                    }
                },
                label = queryPortLabel,
                modifier = Modifier.weight(1f),
                hint = queryPortHint,
                error = !settings.administration.validateQueryPort(),
            )
        }
        CheckboxSectionHeader(
            checked = settings.administration.rconEnabled,
            onCheckedChange = { enabled ->
                component.updateServerAdministrationSettings {
                    it.copy(rconEnabled = enabled)
                }
            },
            label = rconLabel,
            hint = rconHint,
        )
        FormTextField(
            value = settings.administration.rconPort.toString(),
            onValueChange = { newValue ->
                val port = newValue.toIntOrNull()
                if (port != null) {
                    component.updateServerAdministrationSettings {
                        it.copy(rconPort = port)
                    }
                }
            },
            label = rconPortLabel,
            hint = rconPortHint,
            error = if (settings.administration.rconEnabled)
                !settings.administration.validateRconPort() else false,
            enabled = settings.administration.rconEnabled
        )
        FormTextField(
            value = gameUserSettings.serverSettings.rconServerGameLogBuffer.toString(),
            onValueChange = { newValue: String ->
                val buffer = newValue.toIntOrNull()
                if (buffer != null) {
                    component.updateServerGameUserSettings {
                        it.copy(serverSettings = it.serverSettings.copy(rconServerGameLogBuffer = buffer))
                    }
                }
            },
            label = rconServerGameLogBufferLabel,
            hint = rconServerGameLogBufferHint,
            error = !gameUserSettings.serverSettings.validateRconServerGameLogBuffer(),
            enabled = settings.administration.rconEnabled
        )
    }
}

