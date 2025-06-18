package ui.serverList


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.composefluent.FluentTheme
import io.github.composefluent.component.AccentButton
import io.github.composefluent.component.ListItemSeparator
import io.github.composefluent.component.Text
import serverList.components.ServerCard
import ui.ToastManager
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ServerListScreen(component: ServerListComponent) {
    val servers by component.servers.subscribeAsState()
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Server List",
                style = FluentTheme.typography.title,
            )
            Spacer(modifier = Modifier.weight(1f))
            AccentButton(
                onClick = {
                    val result = component.addServer()
                    if (result.isFailure) {
                        ToastManager.get().toast(
                            result.exceptionOrNull()?.message ?: "Failed to add server",
                            type = com.dokar.sonner.ToastType.Error
                        )
                    } else {
                        ToastManager.get().toast("Server added successfully", type = com.dokar.sonner.ToastType.Success)
                    }

                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add Server")
            }
        }
        ListItemSeparator(Modifier.fillMaxWidth().padding(vertical = 8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
            items(servers.size) {
                ServerCard(servers[it]) {
                    component.onServerSelected(servers[it].uuid.toString())
                }
            }
        }
    }
}
