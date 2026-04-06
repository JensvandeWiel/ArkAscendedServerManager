@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.server

import Committing
import Downloading
import Preallocating
import Preparing
import Reconfiguring
import SteamCMDInstalling
import SteamCMDUpdating
import Validating
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.Idle
import eu.wynq.arkascendedservermanager.core.InstallDone
import eu.wynq.arkascendedservermanager.core.InstallingAPI
import eu.wynq.arkascendedservermanager.core.InstallingGame
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.LabelPosition
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.theme.defaultTabStyle
import org.jetbrains.jewel.ui.typography
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ServerScreen(component: ServerComponent) {
    val title = stringResource(Res.string.server_details_title)
    val notFoundLabel = stringResource(Res.string.server_details_not_found)
    val errorLabel = stringResource(Res.string.server_details_error)
    val infoTabLabel = stringResource(Res.string.server_details_info_tab)
    val generalTabLabel = stringResource(Res.string.server_details_general_tab)
    val saveServerLabel = stringResource(Res.string.action_save_server)
    val model by component.model.subscribeAsState()
    val error by component.error.collectAsState()
    val selectedTab by component.selectedTab.subscribeAsState()
    val tabs =
        remember(selectedTab, infoTabLabel, generalTabLabel) {
            listOf(
                TabData.Default(
                    selected = selectedTab == ServerDetailsTab.INFO,
                    content = { tabState ->
                        SimpleTabContent(label = infoTabLabel, state = tabState)
                    },
                    onClick = component::selectInfoTab,
                    closable = false,
                ),
                TabData.Default(
                    selected = selectedTab == ServerDetailsTab.GENERAL,
                    content = { tabState ->
                        SimpleTabContent(label = generalTabLabel, state = tabState)
                    },
                    onClick = component::selectGeneralTab,
                    closable = false,
                ),
            )
        }

    Column {
        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, style = JewelTheme.typography.h2TextStyle)
            Text("/", style = JewelTheme.typography.h2TextStyle)
            Text(model.initialServer?.profileName ?: "", style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::saveServer, enabled = model.isDirty()) {
                Text(saveServerLabel)
            }
        }
        TabStrip(
            tabs = tabs,
            style = JewelTheme.defaultTabStyle,
            modifier = Modifier.fillMaxWidth(),
        )
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            val loadedServer = model.initialServer
            if (loadedServer != null) {
                when (selectedTab) {
                    ServerDetailsTab.INFO -> {
                        InfoTabContent(component)
                    }
                    ServerDetailsTab.GENERAL -> {
                        GeneralTabContent(component)
                    }
                }
            } else if (error != null) {
                Text("$errorLabel: $error")
            } else {
                Text(notFoundLabel)
            }
        }
    }
}

@Composable
fun InfoTabContent(component: ServerComponent) {
    InstallationInfo(component)
}

@Composable
fun InstallProgress(
    text: String,
    progress: Float,
    indeterminate: Boolean = false,
    modifier: Modifier = Modifier,
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    if (indeterminate) {
        IndeterminateHorizontalProgressBar(Modifier.weight(1f).padding(end = 8.dp))
    } else {
        HorizontalProgressBar(progress, Modifier.weight(1f).padding(end = 8.dp))
    }
    val progressText = stringResource(Res.string.server_details_progress_format, text, (progress * 100).toInt())
    Text(progressText)
}

@Composable
fun InstallationInfo(component: ServerComponent) {
    val model by component.model.subscribeAsState()
    val status by component.installStatus.collectAsState()
    val installationInfoGroup = stringResource(Res.string.server_details_installation_info_group)
    val installationStatusLabel = stringResource(Res.string.server_details_installation_status_label)
    val installedStatus = stringResource(Res.string.server_details_installation_status_installed)
    val notInstalledStatus = stringResource(Res.string.server_details_installation_status_not_installed)
    val versionLabel = stringResource(Res.string.server_details_version_label)
    val versionPlaceholder = stringResource(Res.string.server_details_version_placeholder)
    val preparingLabel = stringResource(Res.string.server_details_progress_preparing)
    val installingApiLabel = stringResource(Res.string.server_details_progress_installing_api)
    val updatingSteamCmdLabel = stringResource(Res.string.server_details_progress_updating_steamcmd)
    val installingSteamCmdLabel = stringResource(Res.string.server_details_progress_installing_steamcmd)
    val downloadingLabel = stringResource(Res.string.server_details_progress_downloading)
    val validatingLabel = stringResource(Res.string.server_details_progress_validating)
    val reconfiguringLabel = stringResource(Res.string.server_details_progress_reconfiguring)
    val preallocatingLabel = stringResource(Res.string.server_details_progress_preallocating)
    val committingLabel = stringResource(Res.string.server_details_progress_committing)
    val busyLabel = stringResource(Res.string.server_details_progress_busy)
    val installLabel = stringResource(Res.string.action_install_server)
    val updateLabel = stringResource(Res.string.action_update_server)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        GroupHeader(installationInfoGroup)
        Row {
            Text(installationStatusLabel)
            Spacer(Modifier.weight(1f))
            Text(
                when (model.isInstalled) {
                    true -> installedStatus
                    false -> notInstalledStatus
                    null -> "…"
                }
            )
        }
        Row {
            Text(versionLabel)
            Spacer(Modifier.weight(1f))
            Text(
                when (model.isInstalled) {
                    true -> (if (model.version != null) "v${model.version}" else versionPlaceholder)
                    false -> notInstalledStatus
                    null -> "…"
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(Modifier.weight(1f)) {
                when (status) {
                    is eu.wynq.arkascendedservermanager.core.Preparing -> {
                        InstallProgress(preparingLabel, 0f, indeterminate = true, modifier = Modifier.fillMaxWidth())
                    }

                    is InstallingAPI -> {
                        InstallProgress(installingApiLabel, 0f, indeterminate = true, modifier = Modifier.fillMaxWidth())
                    }

                    is InstallingGame -> {
                        val gameStatus = status as InstallingGame
                        when (gameStatus.status) {
                            is Preparing -> {
                                InstallProgress(preparingLabel, 0f, indeterminate = true, modifier = Modifier.fillMaxWidth())
                            }

                            is SteamCMDUpdating -> {
                                InstallProgress(
                                    updatingSteamCmdLabel,
                                    (gameStatus.status as SteamCMDUpdating).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is SteamCMDInstalling -> {
                                InstallProgress(installingSteamCmdLabel, 0f, indeterminate = true, modifier = Modifier.fillMaxWidth())
                            }

                            is Downloading -> {
                                InstallProgress(
                                    downloadingLabel,
                                    (gameStatus.status as Downloading).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is Validating -> {
                                InstallProgress(
                                    validatingLabel,
                                    (gameStatus.status as Validating).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is Reconfiguring -> {
                                InstallProgress(
                                    reconfiguringLabel,
                                    (gameStatus.status as Reconfiguring).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is Preallocating -> {
                                InstallProgress(
                                    preallocatingLabel,
                                    (gameStatus.status as Preallocating).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is Committing -> {
                                InstallProgress(
                                    committingLabel,
                                    (gameStatus.status as Committing).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            else -> {
                                InstallProgress(busyLabel, 0f, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }

                    is InstallDone -> {
                        // Don't show any progress, finish is reflected using popup.
                    }

                    is Idle -> {
                        // Don't show any progress.
                    }

                    else -> {
                        throw IllegalStateException("Status should be handled before it reaches the ui: $status")
                    }
                }
            }
            LaunchedEffect(status) {
                if (status is InstallDone) {
                    component.refreshInstallationInfo()
                }
            }
            DefaultButton(onClick = component::startInstall, enabled = !status.isInstalling()) {
                Text(if (model.isInstalled == true) updateLabel else installLabel)
            }
        }
    }
}

@Composable
fun GeneralTabContent(component: ServerComponent) {
    val model by component.model.subscribeAsState()
    val profileGroupLabel = stringResource(Res.string.server_details_group_profile)
    val serverGroupLabel = stringResource(Res.string.server_details_group_server)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name_label)
    val installationPathLabel = stringResource(Res.string.server_details_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_details_installation_path_hint)
    val serverNameLabel = stringResource(Res.string.server_details_server_name_label)
    val serverNameHint = stringResource(Res.string.server_details_server_name_hint)
    val asaApiLabel = stringResource(Res.string.server_details_asa_api_label)
    val asaApiHint = stringResource(Res.string.server_details_asa_api_hint)

    GroupHeader(profileGroupLabel)
    FormTextField(
        value = model.server?.profileName ?: "",
        onValueChange = { newValue ->
            component.updateServer {
                it.copy(profileName = newValue)
            }
        },
        label = profileNameLabel,
        error = model.server?.validateProfileName() == false,
        labelPosition = LabelPosition.Above,
    )
    FormTextField(
        value = model.server?.installationLocation ?: "",
        onValueChange = { newValue ->
            component.updateServer {
                it.copy(installationLocation = newValue)
            }
        },
        label = installationPathLabel,
        error = model.server?.validateInstallationLocation() == false,
        hint = installationPathHint,
        labelPosition = LabelPosition.Above,
    )
    GroupHeader(serverGroupLabel)
    FormCheckboxField(
        model.server?.asaApi ?: false,
        onCheckedChange = { newValue ->
            component.updateServer {
                it.copy(asaApi = newValue)
            }
        },
        label = asaApiLabel,
        hint = asaApiHint,
    )
    FormTextField(
        value = model.server?.serverName ?: "",
        onValueChange = { newValue ->
            component.updateServer {
                it.copy(serverName = newValue)
            }
        },
        label = serverNameLabel,
        error = model.server?.validateServerName() == false,
        labelPosition = LabelPosition.Above,
        hint = serverNameHint,
    )
}
