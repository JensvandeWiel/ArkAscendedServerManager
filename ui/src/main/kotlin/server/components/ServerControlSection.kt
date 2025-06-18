package ui.server.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.dokar.sonner.ToastType
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.AccentButton
import io.github.composefluent.component.Text
import kotlinx.coroutines.launch
import ui.ToastManager
import ui.server.ServerComponent

@Composable
fun ServerControlSection(component: ServerComponent) {
    val server by component.server.subscribeAsState()
    val isRunning by component.isRunning.subscribeAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Server is " + if (isRunning) "running" else "not running", style = FluentTheme.typography.body)
        Spacer(modifier = Modifier.weight(1f))
        AccentButton(
            disabled = !server.getServerManager().getInstallManager().isInstalled(),
            onClick = {
                component.coroutineScope().launch {
                    if (isRunning) {
                        val result = component.stopServer()
                        if (result.isFailure) {
                            ToastManager.get().toast(
                                "Failed to stop server: ${result.exceptionOrNull()?.message}",
                                ToastType.Error
                            )
                        } else {
                            ToastManager.get().toast("Server stopped successfully", ToastType.Success)
                        }
                    } else {
                        val result = component.startServer()
                        if (result.isFailure) {
                            ToastManager.get().toast(
                                "Failed to start server: ${result.exceptionOrNull()?.message}",
                                ToastType.Error
                            )
                        } else {
                            ToastManager.get().toast("Server started successfully", ToastType.Success)
                        }
                    }
                }
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(if (isRunning) "Stop Server" else "Start Server")
        }
    }
}
