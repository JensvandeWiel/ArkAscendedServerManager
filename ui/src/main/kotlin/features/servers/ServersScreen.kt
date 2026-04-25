@file:OptIn(ExperimentalUuidApi::class, ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui.features.servers

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.managers.AsaApiInstallManager
import eu.wynq.arkascendedservermanager.core.managers.InstallManager
import eu.wynq.arkascendedservermanager.core.managers.PowerManager
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.ui.components.FormPathField
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import eu.wynq.arkascendedservermanager.ui.helpers.PreviewWrapper
import eu.wynq.arkascendedservermanager.ui.helpers.getPowerStateLabel
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerManager
import eu.wynq.arkascendedservermanager.ui.notifications.ToastBannerType
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedDialog
import io.github.kdroidfilter.nucleus.window.jewel.JewelDialogTitleBar
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServersScreen(component: ServersComponent) {
    val pageServers = stringResource(Res.string.page_servers)
    val pageServersEmptyState = stringResource(Res.string.page_servers_empty_state)
    val actionAddServer = stringResource(Res.string.action_add_server)
    val actionImportServer = stringResource(Res.string.action_import_server)
    val servers = component.servers.collectAsState()
    val model by component.model.subscribeAsState()

    if (model.importServersDialogOpen) {
        ImportServerDialog(
            onImport = component::importServer,
            onDismiss = component::onImportServersDialogDismissed,
        )
    }

    Column {
        Row(Modifier.fillMaxWidth().padding(8.dp)) {
            Text(pageServers, style = JewelTheme.typography.h2TextStyle)
            Spacer(Modifier.weight(1f))
            IconActionButton(
                key = AllIconsKeys.General.Add,
                contentDescription = actionAddServer,
                onClick = component::onServerAddClicked,
                focusable = false,
                tooltip = { Text(actionAddServer) },
                tooltipPlacement = TooltipPlacement.ComponentRect(
                    Alignment.CenterStart,
                    Alignment.CenterStart,
                    offset = DpOffset((-6).dp, 0.dp)
                ),
            )
            IconActionButton(
                key = AllIconsKeys.ToolbarDecorator.Import,
                contentDescription = actionImportServer,
                onClick = component::onImportServerClicked,
                focusable = false,
                tooltip = { Text(actionImportServer) },
                tooltipPlacement = TooltipPlacement.ComponentRect(
                    Alignment.CenterStart,
                    Alignment.CenterStart,
                    offset = DpOffset((-6).dp, 0.dp)
                ),
            )
        }
        Divider(
            Orientation.Horizontal, Modifier
                .fillMaxWidth()
        )
        VerticallyScrollableContainer(Modifier.fillMaxSize()) {
            if (servers.value.values.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(pageServersEmptyState)
                }
            } else {
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    servers.value.values.forEach { server ->
                        val powerState by component.getPowerState(server).collectAsState()
                        val installStatus by component.getInstallStatus(server).collectAsState()

                        ServerCard(
                            server = server,
                            powerState = powerState,
                            canStart = PowerManager.canStartServer(
                                server = server,
                                powerState = powerState,
                                installStatus = installStatus,
                                isInstalled = InstallManager.isInstalled(server),
                                apiIsInstalled = AsaApiInstallManager.isInstalled(server),
                                isOverseerInstalled = AsaApiInstallManager.isOverseerInstalled(server),
                                overseerVersion = AsaApiInstallManager.getOverseerVersionAsString(server),
                                currentAppVersion = AppBuildInfo.version
                            ),
                            canStop = PowerManager.canStopServer(powerState, installStatus),
                            canKill = PowerManager.canKillServer(powerState, installStatus),
                            onStart = { component.startServer(server) },
                            onStop = { component.stopServer(server) },
                            onKill = { component.killServer(server) },
                            onClone = { component.cloneServer(server) },
                            onClick = { component.onServerClicked(server) },
                        )
                    }
                }
            }
        }
    }
}

private fun isInstalledPath(path: String?): Boolean {
    val normalizedPath = path?.takeIf { it.isNotBlank() } ?: return false
    return runCatching { InstallManager.isInstalled(Path.of(normalizedPath)) }
        .onFailure { logger.warn(it) { "Invalid installation path input: $normalizedPath" } }
        .getOrDefault(false)
}

@Composable
fun ServerCard(
    server: Server,
    powerState: PowerState = PowerState.Unknown,
    canStart: Boolean = false,
    canStop: Boolean = false,
    canKill: Boolean = false,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onKill: () -> Unit = {},
    onClone: () -> Unit = {},
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor = if (isHovered) {
        JewelTheme.globalColors.panelBackground.copy(alpha = 0.82f)
    } else {
        JewelTheme.globalColors.panelBackground
    }
    val goToServerDescription = stringResource(Res.string.server_details_go_to_server)

    val startServerLabel = stringResource(Res.string.action_start_server)
    val stopServerLabel = stringResource(Res.string.action_stop_server)
    val killServerLabel = stringResource(Res.string.action_kill_server)
    val cloneServerLabel = "Clone server"

    val statusBarColor = Color(PowerManager.getPowerStateColor(powerState))

    Row(
        Modifier
            .fillMaxWidth()
            .pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clip(RoundedCornerShape(8.dp))
            .height(94.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(6.dp)
                .background(statusBarColor)
        )

        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(server.profileName, style = JewelTheme.typography.h2TextStyle)
                Icon(
                    AllIconsKeys.General.ArrowRight,
                    contentDescription = goToServerDescription,
                    modifier = Modifier.size(24.dp),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getPowerStateLabel(powerState),
                    style = JewelTheme.typography.labelTextStyle,
                    color = JewelTheme.globalColors.text.disabled
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconActionButton(
                        key = AllIconsKeys.General.Layout,
                        contentDescription = cloneServerLabel,
                        onClick = onClone,
                        focusable = false,
                        tooltip = { Text(cloneServerLabel) }
                    )
                    DefaultButton(onClick = onStart, enabled = canStart) {
                        Text(startServerLabel)
                    }
                    DefaultButton(onClick = onStop, enabled = canStop) {
                        Text(stopServerLabel)
                    }
                    DefaultButton(onClick = onKill, enabled = canKill) {
                        Text(killServerLabel)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun ServerCardPreview() {
    val server = Server(
        Uuid.random(), "Profile Name", "Installation Location",
        settings = Settings.createForNewServer(),
        gameUserSettings = GameUserSettings.createForNewServer("Profile Name"),
    )
    PreviewWrapper {
        ServerCard(
            server = server,
            powerState = PowerState.Running,
            canStart = true,
            canStop = true,
            canKill = true,
            onClick = {}
        )
    }
}

@Composable
fun ImportServerDialog(
    onImport: (path: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val importDialogState = rememberDialogState(size = DpSize(460.dp, 250.dp))
    val importServerTitle = stringResource(Res.string.server_import_dialog_title)
    val selectRootFolder = stringResource(Res.string.server_import_select_root_folder)
    val installationPathLabel = stringResource(Res.string.server_import_installation_path_label)
    val installationPathHint = stringResource(Res.string.server_import_installation_path_hint)
    val invalidInstallationPath = stringResource(Res.string.server_import_invalid_installation_path)
    val cancelLabel = stringResource(Res.string.action_cancel)
    val importLabel = stringResource(Res.string.action_import)
    var path by remember { mutableStateOf<String?>(null) }
    val pathIsInvalid by remember {
        derivedStateOf {
            !isInstalledPath(path)
        }
    }
    JewelDecoratedDialog(
        onCloseRequest = onDismiss,
        title = importServerTitle,
        resizable = false,
        state = importDialogState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JewelTheme.globalColors.panelBackground),
        ) {
            JewelDialogTitleBar {
                Text(importServerTitle)
            }
            Row(Modifier.fillMaxWidth().padding(8.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(selectRootFolder)
                    FormPathField(
                        value = path ?: "",
                        onValueChange = {
                            val isInstalled = isInstalledPath(it)
                            if (!isInstalled) {
                                ToastBannerManager.show(ToastBannerType.ERROR, invalidInstallationPath)
                            } else {
                                path = it
                                onImport(it)
                            }
                        },
                        label = installationPathLabel,
                        hint = installationPathHint,
                        error = path != null && pathIsInvalid,
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Spacer(Modifier.weight(1f))
                OutlinedButton(onClick = onDismiss) {
                    Text(cancelLabel)
                }
                DefaultButton(onClick = { path?.let(onImport) }, enabled = path != null && !pathIsInvalid) {
                    Text(importLabel)
                }
            }
        }
    }
}
