@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.jetbrains.jewel.ui.component.styling.LocalTooltipStyle
import org.jetbrains.jewel.ui.component.styling.TooltipAutoHideBehavior
import org.jetbrains.jewel.ui.component.styling.TooltipMetrics
import org.jetbrains.jewel.ui.component.styling.TooltipStyle
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ZeroDelayNeverHideTooltips(content: @Composable () -> Unit) {
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