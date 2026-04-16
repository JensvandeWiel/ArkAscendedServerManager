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
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.managers.*
import eu.wynq.arkascendedservermanager.ui.components.*
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.z4kn4fein.semver.Version
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.theme.defaultTabStyle
import org.jetbrains.jewel.ui.typography
import kotlin.uuid.ExperimentalUuidApi

private val logger = KotlinLogging.logger {}

@Composable
fun ServerScreen(component: ServerComponent) {
    val title = stringResource(Res.string.server_details_title)
    val titleSeparator = stringResource(Res.string.server_details_title_separator)
    val notFoundLabel = stringResource(Res.string.server_details_not_found)
    val errorLabel = stringResource(Res.string.server_details_error)
    val infoTabLabel = stringResource(Res.string.server_details_info_tab)
    val generalTabLabel = stringResource(Res.string.server_details_general_tab)
    val saveServerLabel = stringResource(Res.string.action_save_server)
    val deleteServerLabel = stringResource(Res.string.action_delete_server)
    val deleteDialogTitleFormat = stringResource(Res.string.server_details_delete_dialog_title_format)
    val deleteDialogMessage = stringResource(Res.string.server_details_delete_dialog_message)
    val deleteDialogDefaultTarget = stringResource(Res.string.server_details_delete_dialog_target_default)
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

    if (model.deleteDialogOpen) {
        ConfirmationDialog(
            onConfirm = component::onDeletionDialogConfirmed,
            onDismiss = component::onDeletionDialogDismissed,
            deleteDialogTitleFormat.format(model.initialServer?.profileName ?: deleteDialogDefaultTarget),
            deleteDialogMessage,
        )
    }


    Column {
        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, style = JewelTheme.typography.h2TextStyle)
            Text(titleSeparator, style = JewelTheme.typography.h2TextStyle)
            Text(model.initialServer?.profileName ?: "", style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::saveServer, enabled = model.canSave()) {
                Text(saveServerLabel)
            }
            DefaultButton(onClick = component::openDeleteDialog) {
                Text(deleteServerLabel)
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
    val model by component.model.subscribeAsState()
    InstallationInfo(component)
    if (model.server?.asaApi == true) {
        LogsSection(component)
    }
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
    val overseerVersionLabel = stringResource(Res.string.server_details_overseer_version_label)
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

    fun overSeerIsSameVersion(): Boolean {
        val currentVersion = Version.parse(model.overseerVersion ?: "69.69.69")
        val currentAppVersion = Version.parse(AppBuildInfo.version)
        return currentVersion == currentAppVersion
    }

    val canStartServer =
        !status.isInstalling()
                && (powerState == PowerState.Stopped)
                && (model.isInstalled == true)
                && (if (model.initialServer?.asaApi == true) model.apiIsInstalled == true
                && model.isOverseerInstalled == true else true)
                && overSeerIsSameVersion()
    val canStopServer = !status.isInstalling() && powerState == PowerState.Running
    val canKillServer =
        !status.isInstalling() && (powerState == PowerState.Running || powerState == PowerState.Starting || powerState == PowerState.Stopping)

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
        if (isAsaApiEnabled) {
            Row {
                Text(overseerVersionLabel)
                Spacer(Modifier.weight(1f))
                Text(
                    when (model.isOverseerInstalled) {
                        true -> (if (model.overseerVersion != null) versionValueFormat.format(model.overseerVersion) else versionPlaceholder)
                        false -> notInstalledStatus
                        null -> unknownStatus
                    }
                )
            }
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
                    is eu.wynq.arkascendedservermanager.core.managers.Preparing -> {
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
                        logger.error { "Unexpected install status in UI: $status" }
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
                Text(if (model.isInstalled == true && (if (model.initialServer?.asaApi == true) model.apiIsInstalled else true) == true) updateLabel else installLabel)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.weight(1f))
            DefaultButton(onClick = component::startServer, enabled = canStartServer) {
                Text(startServerLabel)
            }
            DefaultButton(onClick = component::stopServer, enabled = canStopServer) {
                Text(stopServerLabel)
            }
            DefaultButton(onClick = component::killServer, enabled = canKillServer) {
                Text(killServerLabel)
            }
        }
    }
}

@Composable
fun GeneralTabContent(component: ServerComponent) {
    val model by component.model.subscribeAsState()
    val clusters by component.clusters.collectAsState()
    val profileGroupLabel = stringResource(Res.string.server_details_group_profile)
    val clusterLabel = stringResource(Res.string.server_details_cluster_label)
    val nameAndPasswordsGroupLabel = stringResource(Res.string.server_details_group_name_and_passwords)
    val portsGroupLabel = stringResource(Res.string.server_details_group_ports)
    val mapAndModsGroupLabel = stringResource(Res.string.server_details_group_map_and_mods)
    val serverOptionsGroupLabel = stringResource(Res.string.server_details_group_server_options)
    val profileNameLabel = stringResource(Res.string.server_details_profile_name_label)
    val installationPathLabel = stringResource(Res.string.server_details_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_details_installation_path_hint)
    val serverNameLabel = stringResource(Res.string.server_details_server_name_label)
    val serverNameHint = stringResource(Res.string.server_details_server_name_hint)
    val asaApiLabel = stringResource(Res.string.server_details_asa_api_label)
    val asaApiHint = stringResource(Res.string.server_details_asa_api_hint)
    val serverPasswordLabel = stringResource(Res.string.server_details_server_password_label)
    val serverPasswordHint = stringResource(Res.string.server_details_server_password_hint)
    val adminPasswordLabel = stringResource(Res.string.server_details_admin_password_label)
    val adminPasswordHint = stringResource(Res.string.server_details_admin_password_hint)
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
    val mapLabel = stringResource(Res.string.server_details_map_label)
    val mapHint = stringResource(Res.string.server_details_map_hint)
    val modsLabel = stringResource(Res.string.server_details_mods_label)
    val modsHint = stringResource(Res.string.server_details_mods_hint)
    val savesGroupLabel = stringResource(Res.string.server_details_group_saves)
    val autoSavePeriodLabel = stringResource(Res.string.server_details_auto_save_period_label)
    val autoSavePeriodHint = stringResource(Res.string.server_details_auto_save_period_hint)
    val motdGroupLabel = stringResource(Res.string.server_details_group_motd)
    val motdMessageLabel = stringResource(Res.string.server_details_motd_message_label)
    val motdMessageHint = stringResource(Res.string.server_details_motd_message_hint)
    val motdDurationLabel = stringResource(Res.string.server_details_motd_duration_label)
    val motdDurationHint = stringResource(Res.string.server_details_motd_duration_hint)
    val slotsLabel = stringResource(Res.string.server_details_slots_label)
    val slotsHint = stringResource(Res.string.server_details_slots_hint)
    val idlePlayerKickTimeLabel = stringResource(Res.string.server_details_idle_player_kick_time_label)
    val idlePlayerKickTimeHint = stringResource(Res.string.server_details_idle_player_kick_time_hint)
    val idlePlayerKickEnabledLabel = stringResource(Res.string.server_details_idle_player_kick_enabled_label)
    val idlePlayerKickEnabledHint = stringResource(Res.string.server_details_idle_player_kick_enabled_hint)
    val clusterIdLabel = stringResource(Res.string.server_details_cluster_id_label)
    val clusterIdHint = stringResource(Res.string.server_details_cluster_id_hint)
    val clusterDirOverrideLabel = stringResource(Res.string.server_details_cluster_dir_override_label)
    val clusterDirOverrideHint = stringResource(Res.string.server_details_cluster_dir_override_hint)

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
                GroupHeader(mapAndModsGroupLabel)
                FormTextField(
                    value = settings.administration.map,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(map = newValue)
                        }
                    },
                    label = mapLabel,
                    hint = mapHint,
                    error = !settings.administration.validateMap(),
                )
                FormTextField(
                    value = settings.administration.mods.joinToString(","),
                    onValueChange = { newValue ->
                        val mods =
                            newValue.split(",").mapNotNull { rawValue -> rawValue.trim().takeIf { it.isNotEmpty() } }
                        component.updateServerAdministrationSettings {
                            it.copy(mods = mods)
                        }
                    },
                    label = modsLabel,
                    hint = modsHint,
                    error = !settings.administration.validateMods(),
                )
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
                GroupHeader(stringResource(Res.string.clusters_configuration_group))
                FormSelectField(
                    value = cluster,
                    onValueChange = component::updateServerCluster,
                    options = clusters.values.toList(),
                    optionLabel = { clusterOption ->
                        clusterOption?.name ?: runBlocking { getString(Res.string.clusters_none) }
                    },
                    label = clusterLabel,
                    compareWith = { a, b -> a?.id == b?.id }
                )
                FormTextField(
                    value = settings.administration.clusterId ?: "",
                    onValueChange = {},
                    label = clusterIdLabel,
                    readOnly = true,
                    hint = clusterIdHint,
                )
                FormTextField(
                    value = settings.administration.clusterDirOverride ?: "",
                    onValueChange = {},
                    label = clusterDirOverrideLabel,
                    readOnly = true,
                    hint = clusterDirOverrideHint,
                )
                GroupHeader(serverOptionsGroupLabel)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormCheckboxField(
                        settings.options.enableIdlePlayerKick,
                        onCheckedChange = { newValue ->
                            component.updateServerOptions {
                                it.copy(enableIdlePlayerKick = newValue)
                            }
                        },
                        label = idlePlayerKickEnabledLabel,
                        hint = idlePlayerKickEnabledHint,
                    )
                    FormSliderField(
                        value = gameUserSettings.serverSettings.kickIdlePlayersPeriod,
                        onValueChange = { newValue ->
                            component.updateServerGameUserSettings {
                                it.copy(serverSettings = it.serverSettings.copy(kickIdlePlayersPeriod = newValue))
                            }
                        },
                        label = idlePlayerKickTimeLabel,
                        valueRange = 0..7200,
                        hint = idlePlayerKickTimeHint,
                        allowOutsideRange = true,
                        showManualInput = true,
                        enabled = settings.options.enableIdlePlayerKick,
                        error = if (settings.options.enableIdlePlayerKick) !gameUserSettings.serverSettings.validateKickIdlePlayersPeriod() else false,
                    )
                }
                FormSliderField(
                    value = settings.administration.slots,
                    onValueChange = { newValue ->
                        component.updateServerAdministrationSettings {
                            it.copy(slots = newValue)
                        }
                    },
                    label = slotsLabel,
                    valueRange = 1..250,
                    error = !settings.administration.validateSlots(),
                    hint = slotsHint,
                    allowOutsideRange = true,
                    showManualInput = true,
                )
            }
        }
    }
}

@Composable
fun LogsSection(component: ServerComponent) {
    val logs by component.logs.collectAsState()
    val powerState by component.serverPowerState.collectAsState()
    val logsLabel = stringResource(Res.string.server_details_logs_group)

    GroupHeader(logsLabel)
    val listState = rememberLazyListState()
    var viewportHeightPx by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .onSizeChanged { viewportHeightPx = it.height }
                .background(
                    if (isSystemInDarkTheme()) Color(0xff000000) else Color(0xffffffff),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            LazyColumn(
                state = listState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(logs) { line ->
                    Text(
                        line,
                        Modifier.fillMaxWidth(),
                        style = JewelTheme.typography.consoleTextStyle
                    )
                }
            }
        }
        LaunchedEffect(logs.lastOrNull(), viewportHeightPx) {
            if (logs.isNotEmpty()) {
                listState.scrollToItem(logs.lastIndex)
            }
        }
        LaunchedEffect(powerState) {
            if (powerState == PowerState.Stopped) {
                component.clearLogs()
            }
        }
    }
}
