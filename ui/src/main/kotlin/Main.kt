package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import eu.wynq.arkascendedservermanager.components.AppTheme
import eu.wynq.arkascendedservermanager.components.components.Scaffold
import eu.wynq.arkascendedservermanager.components.components.Text

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Compose MPP UI") {
        AppTheme(true) {
            Scaffold {
                Text("Hello, World!")
            }
        }
    }
}