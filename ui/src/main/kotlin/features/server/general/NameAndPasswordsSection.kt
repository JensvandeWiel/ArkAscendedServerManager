package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun NameAndPasswordsSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val nameAndPasswordsGroupLabel = stringResource(Res.string.server_details_group_name_and_passwords)
    val serverNameLabel = stringResource(Res.string.server_details_server_name_label)
    val serverNameHint = stringResource(Res.string.server_details_server_name_hint)
    val serverPasswordLabel = stringResource(Res.string.server_details_server_password_label)
    val serverPasswordHint = stringResource(Res.string.server_details_server_password_hint)
    val adminPasswordLabel = stringResource(Res.string.server_details_admin_password_label)
    val adminPasswordHint = stringResource(Res.string.server_details_admin_password_hint)

    model.server?.run {
        GroupHeader(nameAndPasswordsGroupLabel)
        FormTextField(
            value = gameUserSettings.sessionSettings.sessionName,
            onValueChange = { newValue ->
                component.updateServerGameUserSettings {
                    it.copy(sessionSettings = it.sessionSettings.copy(sessionName = newValue))
                }
            },
            label = serverNameLabel,
            hint = serverNameHint,
            error = !gameUserSettings.sessionSettings.validateSessionName(),
        )
        FormTextField(
            value = settings.administration.serverPassword ?: "",
            onValueChange = { newValue ->
                val normalizedPassword = newValue.trim().takeIf { it.isNotEmpty() }
                component.updateServerAdministrationSettings {
                    it.copy(serverPassword = normalizedPassword)
                }
            },
            label = serverPasswordLabel,
            hint = serverPasswordHint,
            error = !settings.administration.validateServerPassword(),
        )
        FormTextField(
            value = settings.administration.adminPassword,
            onValueChange = { newValue ->
                component.updateServerAdministrationSettings {
                    it.copy(adminPassword = newValue.trim())
                }
            },
            label = adminPasswordLabel,
            hint = adminPasswordHint,
            error = !settings.administration.validateAdminPassword(),
        )
    }
}
