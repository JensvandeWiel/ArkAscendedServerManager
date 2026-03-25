package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.application
import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.nunito
import io.github.kdroidfilter.nucleus.darkmodedetector.isSystemInDarkMode
import io.github.kdroidfilter.nucleus.window.TitleBar
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedWindow
import org.jetbrains.compose.resources.Font
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarStyle

@Composable
fun defaultTextStyle(): TextStyle =
    TextStyle(
        fontFamily =
            FontFamily(
                Font(resource = Res.font.nunito),
            ),
    )

fun main() = application {
    System.setProperty("skiko.renderApi", "OPENGL")
    val isDark = isSystemInDarkMode()

    val themeDefinition = if (isDark) JewelTheme.darkThemeDefinition(
        defaultTextStyle = defaultTextStyle()
    ) else JewelTheme.lightThemeDefinition(
        defaultTextStyle = defaultTextStyle()
    )

    val styling = ComponentStyling.default()
        .decoratedWindow(
            titleBarStyle = if (isDark) TitleBarStyle.dark() else TitleBarStyle.light()
        )

    IntUiTheme(theme = themeDefinition, styling = styling) {
        JewelDecoratedWindow(
            onCloseRequest = { exitApplication() },
            title = "AASM",
        ) {
            Column(Modifier.fillMaxSize().background(JewelTheme.globalColors.panelBackground)) {
                TitleBar(Modifier.newFullscreenControls(), gradientStartColor = Color(0x56fff100)) {
                    org.jetbrains.jewel.ui.component.Text(
                        "Ark Ascended Server Manager"
                    )
                }
                org.jetbrains.jewel.ui.component.Text("Hello World")
            }
        }
    }
}