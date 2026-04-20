package eu.wynq.arkascendedservermanager.ui.features.server

import Committing
import Downloading
import Preallocating
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.managers.*
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.z4kn4fein.semver.Version
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.typography

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
    modifier: Modifier = Modifier.Companion,
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
                    is Preparing -> {
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
                        val logger = KotlinLogging.logger {}
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