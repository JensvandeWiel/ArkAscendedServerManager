package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.Text

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Compose MPP UI") {
        Text("Hello from Compose Multiplatform UI!")
    }
}