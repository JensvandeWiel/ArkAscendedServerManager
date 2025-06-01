package root

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.konyaco.fluent.ExperimentalFluentApi
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.background.Mica
import com.konyaco.fluent.component.Icon
import com.konyaco.fluent.component.SideNav
import com.konyaco.fluent.component.SideNavItem
import com.konyaco.fluent.component.Text
import com.konyaco.fluent.darkColors
import com.konyaco.fluent.icons.Icons
import com.konyaco.fluent.icons.regular.Server
import com.konyaco.fluent.icons.regular.Settings
import com.konyaco.fluent.surface.Card
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
