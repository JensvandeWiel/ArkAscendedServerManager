package ui.server

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.component.*
import components.CollapsibleCard

@Composable
fun ServerScreen(component: ServerComponent) {

    val profileConfigurationModel by component.profileConfigurationModel.subscribeAsState()
    val server by component.server.subscribeAsState()
    val installationModel by component.installationModel.subscribeAsState()
    val administrationModel by component.administrationModel.subscribeAsState()
    val scrollState = rememberScrollState()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp, 0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Profile: " + server.profileName, style = FluentTheme.typography.body)
                Spacer(modifier = Modifier.weight(1f))
                AccentButton({
                    component.saveProfileConfiguration()
                }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Save")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (installationModel.isInstalling) {
                    Text(
                        "Installation status: " + installationModel.message.orEmpty(),
                        style = FluentTheme.typography.body
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (installationModel.progress != null) {
                        ProgressBar(
                            progress = installationModel.progress!! / 100f,
                            modifier = Modifier.width(128.dp).padding(start = 8.dp)
                        )
                    } else {
                        ProgressBar(Modifier.width(128.dp))
                    }
                } else {
                    Text(
                        "Installation status: " + if (server.getServerManager().getInstallManager()
                                .isInstalled()
                        ) "Installed" else "Not Installed"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AccentButton(
                        onClick = {
                            component.startServerInstallation()
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(if (server.getServerManager().getInstallManager().isInstalled()) "Update" else "Install")
                    }
                }
            }
        }
        ListItemSeparator(Modifier)
        Column(Modifier.verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CollapsibleCard(
                title = { Text("Profile", style = FluentTheme.typography.bodyStrong) },
                initiallyExpanded = false
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextField(
                        value = profileConfigurationModel.profileName,
                        onValueChange = { newValue ->
                            component.updateProfileConfigurationModel(
                                component.profileConfigurationModel.value.copy(profileName = newValue)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        header = { Text("Profile Name") },
                    )
                    TextField(
                        value = profileConfigurationModel.installationLocation,
                        onValueChange = { newValue ->
                            component.updateProfileConfigurationModel(
                                component.profileConfigurationModel.value.copy(installationLocation = newValue)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        header = { Text("Installation Location") },
                    )
                }
            }
            CollapsibleCard(
                title = { Text("Administration", style = FluentTheme.typography.bodyStrong) },
                initiallyExpanded = false
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextField(
                        value = administrationModel.serverName,
                        onValueChange = { newValue ->
                            component.updateAdministrationModel(
                                component.administrationModel.value.copy(serverName = newValue)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        header = { Text("Server Name") },
                    )
                    TextField(
                        value = administrationModel.serverPassword,
                        onValueChange = { newValue ->
                            component.updateAdministrationModel(
                                component.administrationModel.value.copy(serverPassword = newValue)
                            )
                        },
                        enabled = administrationModel.serverPasswordEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        header = {
                            CheckBox(
                                administrationModel.serverPasswordEnabled,
                                "Server Password",
                                onCheckStateChange = {
                                    component.updateAdministrationModel(
                                        administrationModel.copy(
                                            serverPasswordEnabled = it,
                                            serverPassword = TextFieldValue("")
                                        )
                                    )
                                }
                            )
                        },
                    )
                    TextField(
                        value = administrationModel.adminPassword,
                        onValueChange = { newValue ->
                            component.updateAdministrationModel(
                                component.administrationModel.value.copy(adminPassword = newValue)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        header = { Text("Admin Password") },
                    )
                    TextField(
                        value = administrationModel.map,
                        onValueChange = { newValue ->
                            component.updateAdministrationModel(
                                component.administrationModel.value.copy(map = newValue)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        header = { Text("Map") },
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            TextField(
                                value = administrationModel.serverPort,
                                onValueChange = { newValue ->
                                    component.updateAdministrationModel(
                                        component.administrationModel.value.copy(serverPort = newValue)
                                    )
                                },
                                header = { Text("Server Port") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            TextField(
                                value = administrationModel.queryPort,
                                onValueChange = { newValue ->
                                    component.updateAdministrationModel(
                                        component.administrationModel.value.copy(queryPort = newValue)
                                    )
                                },
                                header = { Text("Query Port") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Switcher(
                            checked = administrationModel.rconEnabled,
                            onCheckStateChange = { newValue ->
                                component.updateAdministrationModel(
                                    component.administrationModel.value.copy(rconEnabled = newValue)
                                )
                            }
                        )
                        Text("RCON", style = FluentTheme.typography.bodyStrong)
                    }
                    ListItemSeparator(Modifier)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            TextField(
                                enabled = administrationModel.rconEnabled,
                                value = administrationModel.rconPort,
                                onValueChange = { newValue ->
                                    component.updateAdministrationModel(
                                        component.administrationModel.value.copy(rconPort = newValue)
                                    )
                                },
                                header = { Text("RCON Port") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            TextField(
                                enabled = administrationModel.rconEnabled,
                                value = administrationModel.rconPassword,
                                onValueChange = { newValue ->
                                    component.updateAdministrationModel(
                                        component.administrationModel.value.copy(rconPassword = newValue)
                                    )
                                },
                                header = { Text("RCON Password") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
