@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server.general

import androidx.compose.runtime.Composable
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ProfileSection(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val profileGroupLabel = stringResource(Res.string.server_details_group_profile)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name_label)
    val installationPathLabel = stringResource(Res.string.server_details_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_details_installation_path_hint)
    val asaApiLabel = stringResource(Res.string.server_details_asa_api_label)
    val asaApiHint = stringResource(Res.string.server_details_asa_api_hint)

    model.server?.run {
        GroupHeader(profileGroupLabel)
        FormTextField(
            value = profileName,
            onValueChange = { newValue ->
                component.updateServer {
                    it.copy(profileName = newValue)
                }
            },
            label = profileNameLabel,
            error = !validateProfileName(),
        )
        FormTextField(
            value = installationLocation,
            onValueChange = { newValue ->
                component.updateServer {
                    it.copy(installationLocation = newValue)
                }
            },
            label = installationPathLabel,
            hint = installationPathHint,
            error = !validateInstallationLocation(),
        )
        FormCheckboxField(
            asaApi,
            onCheckedChange = { newValue ->
                component.updateServer {
                    it.copy(asaApi = newValue)
                }
            },
            label = asaApiLabel,
            hint = asaApiHint,
        )
    }
}

