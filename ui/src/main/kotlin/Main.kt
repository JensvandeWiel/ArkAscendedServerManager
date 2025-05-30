package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.konyaco.fluent.ExperimentalFluentApi
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.darkColors
import com.konyaco.fluent.lightColors
import ui.root.RootComponent
import root.RootContent

@OptIn(ExperimentalDecomposeApi::class, ExperimentalFluentApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponent = RootComponent(DefaultComponentContext(lifecycle))

    application {
        val windowState = rememberWindowState()

        // Bind the lifecycle of the root component to the window
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            title = "Ark Ascended Server Manager",
            state = windowState
        ) {
            RootContent(rootComponent)
        }
    }
}
