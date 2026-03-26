@file:OptIn(ExperimentalFoundationApi::class)

package ui.features.root

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.jetbrains.jewel.ui.component.SelectableIconActionButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.painter.hints.Size
import ui.components.Card
import ui.components.ZeroDelayNeverHideTooltips
import ui.theme.ThemeUtils.isDarkTheme
import ui.utils.AccentColor

@Composable
fun RootScreen(component: RootComponent) {
    Children(component.stack) { child ->
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 4.dp)) {
            Column(Modifier.padding(vertical = 4.dp).fillMaxHeight()) {
                component.topPages.forEach { page ->
                    NavigationButton(page = page, component = component)
                }

                Spacer(Modifier.weight(1f))

                component.bottomPages.forEach { page ->
                    NavigationButton(page = page, component = component)
                }
            }
            Spacer(Modifier.width(4.dp))
            Card(modifier = Modifier.weight(1f).fillMaxHeight()) {
                val config = child.configuration as? RootComponent.Config
                val page = if (config != null) component.pageFor(config) else null
                if (page != null) {
                    page.render(child.instance)
                } else {
                    Text("Unknown page")
                }
            }
        }
    }
}

@Composable
private fun NavigationButton(page: Page<out RootComponent.Child>, component: RootComponent) {
    ZeroDelayNeverHideTooltips {
        SelectableIconActionButton(
            page.iconKey,
            page.title,
            onClick = { component.navigateTo(page.config) },
            selected = component.currentActive(page.config),
            tooltip = { Text(page.title) },
            tooltipPlacement = TooltipPlacement.ComponentRect(
                Alignment.CenterEnd,
                Alignment.CenterEnd,
                offset = DpOffset(6.dp, 0.dp)
            ),
            extraHints = arrayOf(Size(20)),
            modifier = Modifier.size(32.dp),
        )
    }
}