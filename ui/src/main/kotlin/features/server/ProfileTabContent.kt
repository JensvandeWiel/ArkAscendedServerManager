package eu.wynq.arkascendedservermanager.ui.features.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.server_details_asa_api_hint
import arkascendedservermanager.ui.generated.resources.server_details_asa_api_label
import arkascendedservermanager.ui.generated.resources.server_details_group_profile
import arkascendedservermanager.ui.generated.resources.server_details_installation_path_hint
import arkascendedservermanager.ui.generated.resources.server_details_installation_path_label
import arkascendedservermanager.ui.generated.resources.server_details_profile_name_label
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.features.server.general.ClusterSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.ClusterTributeSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.MapAndModsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.MotdSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.NameAndPasswordsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.PortsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.SavesSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.ServerOptionsSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.TributeDownloadSection
import eu.wynq.arkascendedservermanager.ui.features.server.general.TributeUploadSection
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ProfileTabContent(component: ServerComponent) {
    val model = component.model.subscribeAsState().value
    val profileGroupLabel = stringResource(Res.string.server_details_group_profile)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name_label)
    val installationPathLabel = stringResource(Res.string.server_details_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_details_installation_path_hint)
    val asaApiLabel = stringResource(Res.string.server_details_asa_api_label)
    val asaApiHint = stringResource(Res.string.server_details_asa_api_hint)

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(end = scrollbarContentSafePadding()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
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
    }
}

