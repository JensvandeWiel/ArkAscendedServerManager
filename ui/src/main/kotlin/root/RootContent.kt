package root

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import io.github.composefluent.ExperimentalFluentApi
import io.github.composefluent.FluentTheme
import io.github.composefluent.background.Mica
import io.github.composefluent.component.Icon
import io.github.composefluent.component.SideNav
import io.github.composefluent.component.SideNavItem
import io.github.composefluent.component.Text
import io.github.composefluent.darkColors
import io.github.composefluent.icons.Icons
import io.github.composefluent.icons.regular.Server
import io.github.composefluent.icons.regular.Settings
import io.github.composefluent.surface.Card
import ui.ToastHost
import ui.root.RootComponent
import ui.server.ServerScreen
import ui.serverList.ServerListScreen
import ui.settings.SettingsScreen

@OptIn(ExperimentalFluentApi::class)
@Composable
fun RootContent(component: RootComponent) {

    var isOpen by remember { mutableStateOf(false) }

    Children(
        stack = component.stack,
    ) { child ->
        FluentTheme(darkColors()) {
            Box(Modifier.fillMaxSize()) {
                Mica(Modifier.fillMaxSize()) {
                    Row(
                        Modifier.fillMaxSize()
                    ) {
                        SideNav(
                            modifier = Modifier.fillMaxHeight(),
                            expanded = isOpen,
                            onExpandStateChange = {
                                isOpen = !isOpen
                            },
                            title = { Text("AASM") },
                            footer = {
                                SideNavItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Regular.Settings,
                                            contentDescription = "Settings"
                                        )
                                    },
                                    selected = component.isSettingsActive(),
                                    onClick = {
                                        component.navigateToSettings()
                                    },
                                    content = { Text("Settings") }
                                )
                            }
                        ) {
                            SideNavItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Regular.Server,
                                        contentDescription = "Servers"
                                    )
                                },
                                selected = component.isServerListActive(),
                                onClick = {
                                    component.navigateToServerList()
                                },
                                content = { Text("Servers") }
                            )
                        }
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 0.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            ) {
                                when (val instance = child.instance) {
                                    is RootComponent.Child.Settings -> SettingsScreen(instance.component)
                                    is RootComponent.Child.ServerList -> ServerListScreen(instance.component)
                                    is RootComponent.Child.Server -> ServerScreen(
                                        instance.component
                                    )
                                }
                            }
                        }
                    }
                }
                ToastHost()
            }
        }
    }
}
