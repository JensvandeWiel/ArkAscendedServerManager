package eu.wynq.arkascendedservermanager.ui.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils.buildComponentStyling
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils.buildThemeDefinition
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    val themeDefinition = buildThemeDefinition(ThemeUtils.ThemeMode.Dark)
    val styling = buildComponentStyling(ThemeUtils.ThemeMode.Dark)
    IntUiTheme(themeDefinition, styling) {
        Column(Modifier.fillMaxSize().background(JewelTheme.globalColors.toolwindowBackground)) {
            content()
        }
    }
}