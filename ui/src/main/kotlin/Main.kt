@file:OptIn(ExperimentalFoundationApi::class)

package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedWindow
import io.github.kdroidfilter.nucleus.window.jewel.JewelTitleBar
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.Text
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import org.jetbrains.jewel.window.newFullscreenControls
import ui.features.root.RootComponent
import ui.features.root.RootScreen
import ui.theme.ThemeUtils
import ui.theme.ThemeUtils.buildComponentStyling
import ui.theme.ThemeUtils.buildThemeDefinition

fun main() {
    val lifecycle = LifecycleRegistry()

    val rootComponent = RootComponent(DefaultComponentContext(lifecycle))

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        val themeDefinition = buildThemeDefinition()

        val styling = buildComponentStyling()


        IntUiTheme(theme = themeDefinition, styling = styling) {
            JewelDecoratedWindow(
                state = windowState,
                onCloseRequest = { exitApplication() },
                title = "AASM",
            ) {
                Column(Modifier.fillMaxSize().background(JewelTheme.globalColors.panelBackground)) {
                    JewelTitleBar(
                        Modifier.newFullscreenControls(),
                        gradientStartColor = ThemeUtils.titleBarGradientColor()
                    ) {
                        Row(Modifier.align(Alignment.Start).padding(start = 8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        "Ark Ascended Server Manager",
                                    )
                                }
                            }
                        }
                    }
                    RootScreen(rootComponent)
                }
            }
        }
    }
}