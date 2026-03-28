@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.app_name
import arkascendedservermanager.ui.generated.resources.app_short_name
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedWindow
import io.github.kdroidfilter.nucleus.window.jewel.JewelTitleBar
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.Text
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import eu.wynq.arkascendedservermanager.core.support.LoggerConfigurator.configureLogging
import org.jetbrains.jewel.window.newFullscreenControls
import eu.wynq.arkascendedservermanager.ui.features.root.RootComponent
import eu.wynq.arkascendedservermanager.ui.features.root.RootScreen
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils.buildComponentStyling
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils.buildThemeDefinition
import org.jetbrains.compose.resources.stringResource

fun main() {
    configureLogging(System.getProperty("user.home") + "/.aasm/app.log")

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
                title = stringResource(Res.string.app_short_name),
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
                                        stringResource(Res.string.app_name),
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