@file:OptIn(ExperimentalFoundationApi::class)

package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedWindow
import io.github.kdroidfilter.nucleus.window.jewel.JewelTitleBar
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.SelectableIconActionButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.styling.LocalTooltipStyle
import org.jetbrains.jewel.ui.component.styling.TooltipAutoHideBehavior
import org.jetbrains.jewel.ui.component.styling.TooltipMetrics
import org.jetbrains.jewel.ui.component.styling.TooltipStyle
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.painter.hints.Size
import org.jetbrains.jewel.window.newFullscreenControls
import ui.components.Card
import ui.theme.ThemeUtils
import ui.theme.ThemeUtils.buildComponentStyling
import ui.theme.ThemeUtils.buildThemeDefinition
import ui.theme.ThemeUtils.isDarkTheme
import ui.utils.AccentColor
import kotlin.time.Duration.Companion.milliseconds

fun main() = application {
    val themeDefinition = buildThemeDefinition()

    val styling = buildComponentStyling()


    IntUiTheme(theme = themeDefinition, styling = styling) {
        JewelDecoratedWindow(
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

                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 4.dp)) {
                    Column(Modifier.padding(vertical = 4.dp).fillMaxHeight()) {
                        ZeroDelayNeverHideTooltips {
                            SelectableIconActionButton(
                                AllIconsKeys.Toolwindows.ToolWindowServer,
                                "Servers",
                                onClick = {},
                                selected = true,
                                tooltip = { Text("Servers") },
                                tooltipPlacement = TooltipPlacement.ComponentRect(
                                    Alignment.CenterEnd,
                                    Alignment.CenterEnd,
                                    offset = DpOffset(6.dp, 0.dp)
                                ),
                                extraHints = arrayOf(Size(20)),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        ZeroDelayNeverHideTooltips {
                            SelectableIconActionButton(
                                AllIconsKeys.General.Settings,
                                "Settings",
                                onClick = {},
                                selected = false,
                                tooltip = { Text("Settings") },
                                tooltipPlacement = TooltipPlacement.ComponentRect(
                                    Alignment.CenterEnd,
                                    Alignment.CenterEnd,
                                    offset = DpOffset(6.dp, 0.dp)
                                ),
                                extraHints = arrayOf(Size(20)),
                                modifier = Modifier.size(32.dp),
                                colorFilter = ColorFilter.tint(AccentColor.Default.resolveColor(isDarkTheme()))

                            )
                        }
                    }

                    Spacer(Modifier.width(4.dp))
                    Card(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    }
                }
            }
        }
    }
}

@Composable
private fun ZeroDelayNeverHideTooltips(content: @Composable () -> Unit) {
    val currentTooltipStyle = LocalTooltipStyle.current
    val updatedStyle =
        remember(currentTooltipStyle) {
            TooltipStyle(
                colors = currentTooltipStyle.colors,
                metrics =
                    with(currentTooltipStyle.metrics) {
                        TooltipMetrics(
                            contentPadding = contentPadding,
                            showDelay = 0.milliseconds,
                            cornerSize = cornerSize,
                            borderWidth = borderWidth,
                            shadowSize = shadowSize,
                            placement = placement,
                            regularDisappearDelay = regularDisappearDelay,
                            fullDisappearDelay = fullDisappearDelay,
                        )
                    },
                autoHideBehavior = TooltipAutoHideBehavior.Never,
            )
        }

    CompositionLocalProvider(LocalTooltipStyle provides updatedStyle, content = content)
}