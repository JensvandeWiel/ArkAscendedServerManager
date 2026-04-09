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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.Idle
import eu.wynq.arkascendedservermanager.core.InstallDone
import eu.wynq.arkascendedservermanager.core.InstallingAPI
import eu.wynq.arkascendedservermanager.core.InstallingGame
import eu.wynq.arkascendedservermanager.core.support.PowerState
import eu.wynq.arkascendedservermanager.ui.components.CheckboxSectionHeader
import eu.wynq.arkascendedservermanager.ui.components.FormCheckboxField
import eu.wynq.arkascendedservermanager.ui.components.FormSliderField
import eu.wynq.arkascendedservermanager.ui.components.FormTextField
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
            DefaultButton(onClick = component::saveServer, enabled = model.canSave()) {
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
                Text(stringResource(Res.string.server_details_error_format, errorLabel, error ?: ""))
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
    val powerState by component.serverPowerState.collectAsState()
    val isAsaApiEnabled = model.server?.asaApi ?: model.initialServer?.asaApi ?: false
    val installationInfoGroup = stringResource(Res.string.server_details_installation_info_group)
    val installationStatusLabel = stringResource(Res.string.server_details_installation_status_label)
    val powerStateLabel = stringResource(Res.string.server_details_power_state_label)
    val installedStatus = stringResource(Res.string.server_details_installation_status_installed)
    val notInstalledStatus = stringResource(Res.string.server_details_installation_status_not_installed)
    val powerStateRunning = stringResource(Res.string.server_details_power_state_running)
    val powerStateStarting = stringResource(Res.string.server_details_power_state_starting)
    val powerStateStopping = stringResource(Res.string.server_details_power_state_stopping)
    val powerStateStopped = stringResource(Res.string.server_details_power_state_stopped)
    val powerStateUnknown = stringResource(Res.string.server_details_power_state_unknown)
    val versionLabel = stringResource(Res.string.server_details_version_label)
    val apiVersionLabel = stringResource(Res.string.server_details_api_version_label)
    val versionPlaceholder = stringResource(Res.string.server_details_version_placeholder)
    val versionValueFormat = stringResource(Res.string.server_details_version_value_format)
    val apiNotInstalledStatus = stringResource(Res.string.server_details_api_not_installed)
    val notEnabledStatus = stringResource(Res.string.server_details_not_enabled)
    val unknownStatus = stringResource(Res.string.server_details_unknown_short)
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
    val startServerLabel = stringResource(Res.string.action_start_server)
    val stopServerLabel = stringResource(Res.string.action_stop_server)
    val killServerLabel = stringResource(Res.string.action_kill_server)

    val canStartServer = !status.isInstalling() && (powerState == PowerState.Stopped)
    val canStopServer = !status.isInstalling() && powerState == PowerState.Running
    val canKillServer = !status.isInstalling() && (powerState == PowerState.Running || powerState == PowerState.Starting || powerState == PowerState.Stopping)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        GroupHeader(installationInfoGroup)
        Row {
            Text(installationStatusLabel)
            Spacer(Modifier.weight(1f))
            Text(
                when (model.isInstalled) {
                    true -> {
                        if (isAsaApiEnabled) {
                            when (model.apiIsInstalled) {
                                true -> installedStatus
                                false -> apiNotInstalledStatus
                                else -> unknownStatus
                            }
                        } else {
                            installedStatus
                        }
                    }

                    false -> notInstalledStatus
                    null -> unknownStatus
                }
            )
        }
        Row {
            Text(versionLabel)
            Spacer(Modifier.weight(1f))
            Text(
                when (model.isInstalled) {
                    true -> (if (model.version != null) versionValueFormat.format(model.version) else versionPlaceholder)
                    false -> notInstalledStatus
                    null -> unknownStatus
                }
            )
        }
        Row {
            Text(apiVersionLabel)
            Spacer(Modifier.weight(1f))
            Text(
                if (!isAsaApiEnabled) {
                    notEnabledStatus
                } else {
                    when (model.apiIsInstalled) {
                        true -> (if (model.apiVersion != null) versionValueFormat.format(model.apiVersion) else versionPlaceholder)
                        false -> notInstalledStatus
                        null -> unknownStatus
                    }
                }
            )
        }
        Row {
            Text(powerStateLabel)
            Spacer(Modifier.weight(1f))
            Text(
                when (powerState) {
                    PowerState.Running -> powerStateRunning
                    PowerState.Starting -> powerStateStarting
                    PowerState.Stopping -> powerStateStopping
                    PowerState.Stopped -> powerStateStopped
                    PowerState.Unknown -> powerStateUnknown
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
                        InstallProgress(
                            installingApiLabel,
                            0f,
                            indeterminate = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is InstallingGame -> {
                        val gameStatus = status as InstallingGame
                        when (gameStatus.status) {
                            is Preparing -> {
                                InstallProgress(
                                    preparingLabel,
                                    0f,
                                    indeterminate = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            is SteamCMDUpdating -> {
                                InstallProgress(
                                    updatingSteamCmdLabel,
                                    (gameStatus.status as SteamCMDUpdating).progress / 100f,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is SteamCMDInstalling -> {
                                InstallProgress(
                                    installingSteamCmdLabel,
                                    0f,
                                    indeterminate = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
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
            DefaultButton(onClick = component::startServer, enabled = canStartServer) {
                Text(startServerLabel)
            }
            DefaultButton(onClick = component::stopServer, enabled = canStopServer) {
                Text(stopServerLabel)
            }
            DefaultButton(onClick = component::killServer, enabled = canKillServer) {
                Text(killServerLabel)
            }

            Spacer(Modifier.weight(1f))
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
    stringResource(Res.string.server_details_group_server)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name_label)
    val installationPathLabel = stringResource(Res.string.server_details_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_details_installation_path_hint)
    val serverNameLabel = stringResource(Res.string.server_details_server_name_label)
    val serverNameHint = stringResource(Res.string.server_details_server_name_hint)
    val asaApiLabel = stringResource(Res.string.server_details_asa_api_label)
    val asaApiHint = stringResource(Res.string.server_details_asa_api_hint)

    VerticallyScrollableContainer {
        Column(
            modifier = Modifier.padding(8.dp).padding(end = scrollbarContentSafePadding()),
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
                GroupHeader("Name and passwords")
                FormTextField(
                    value = settings.administration.serverName,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(serverName = newValue)
                        }
                    },
                    label = serverNameLabel,
                    hint = serverNameHint,
                    error = !settings.administration.validateServerName(),
                )
                FormTextField(
                    value = settings.administration.serverPassword ?: "",
                    onValueChange = { newValue ->
                        val normalizedPassword = newValue.trim().takeIf { it.isNotEmpty() }
                        component.updateServerAdministrationSettings {
                            it.copy(serverPassword = normalizedPassword)
                        }
                    },
                    label = "Server password:",
                    hint = "The password users must enter in order to access the server. If this is set, only people who know the password can access your server.",
                    error = !settings.administration.validateServerPassword(),
                )
                FormTextField(
                    value = settings.administration.adminPassword,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(adminPassword = newValue.trim())
                        }
                    },
                    label = "Admin password:",
                    hint = "The password users must enter to execute admin/cheat commands on the server. If this is not set, anyone can use cheats.",
                    error = !settings.administration.validateAdminPassword(),
                )
                GroupHeader("Ports")
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
                                    it.copy(serverPort = port, peerPort = port + 1)
                                }
                            }
                        },
                        label = "Server port:",
                        modifier = Modifier.weight(1f),
                        hint = "The port users will use to connect to your server. Default is 7777. Change this and Query Port if you wish to host multiple servers on one machine.",
                        error = !settings.administration.validateServerPort(),
                    )
                    FormTextField(
                        value = settings.administration.peerPort.toString(),
                        onValueChange = { /* Peer port is always server port +1, so it is not editable. */ },
                        label = "Peer port:",
                        modifier = Modifier.weight(1f),
                        hint = "Peer port (always Game port +1), used for connecting to the server from the client.",
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
                        label = "Query port:",
                        modifier = Modifier.weight(1f),
                        hint = "The port Steam uses to communicate with your server. Default is 27015. Change this and Server Port if you wish to host multiple servers on one machine.",
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
                    label = "RCON",
                    hint = "Enable RCON (Remote Console) for this server. This allows you to execute admin/cheat commands on the server.",
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
                    label = "RCON port:",
                    hint = "The port that RCON will use on your server.",
                    error = if (settings.administration.rconEnabled)
                        !settings.administration.validateRconPort() else false,
                    enabled = settings.administration.rconEnabled
                )
                GroupHeader("Map and Mods")
                FormTextField(
                    value = settings.administration.map,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(map = newValue)
                        }
                    },
                    label = "Map:",
                    hint = "The map your server will load. Make sure to use the correct map name, otherwise your server might not start.",
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
                    label = "Mods (comma separated):",
                    hint = "A comma-separated list of mod ids, in the order in which they should be applied.",
                    error = !settings.administration.validateMods(),
                )
                GroupHeader("Server options")
                FormSliderField(
                    value = settings.administration.slots,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(slots = newValue)
                        }
                    },
                    label = "Slots:",
                    valueRange = 1..250,
                    error = !settings.administration.validateSlots(),
                    hint = "Sets the maximum number of players which can join this server.",
                    allowOutsideRange = true,
                    showManualInput = true,
                )
            }
        }
    }
}