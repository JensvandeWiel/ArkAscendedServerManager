package root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.konyaco.fluent.icons.regular.Home
import com.konyaco.fluent.icons.regular.Settings
import com.konyaco.fluent.lightColors
import com.konyaco.fluent.surface.Card
import ui.main.MainScreen
import ui.root.RootComponent
import ui.settings.SettingsScreen

@OptIn(ExperimentalFluentApi::class)
@Composable
fun RootContent(component: RootComponent) {

    var isOpen by remember { mutableStateOf(false) }

    Children(
        stack = component.stack,
    ) { child ->
        FluentTheme(darkColors()) {
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
                                    imageVector = Icons.Regular.Home,
                                    contentDescription = "Home"
                                )
                            },
                            selected = component.isMainActive(),
                            onClick = {
                                component.navigateToMain()
                            },
                            content = { Text("Main") }
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
                                is RootComponent.Child.Main -> MainScreen(instance.component)
                                is RootComponent.Child.Settings -> SettingsScreen(instance.component)
                            }
                        }
                    }
                }
            }
        }
    }
}
