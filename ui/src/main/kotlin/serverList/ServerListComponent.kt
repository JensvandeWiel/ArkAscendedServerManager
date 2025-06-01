package ui.serverList

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import server.ServerProfile

class ServerListComponent(
    componentContext: ComponentContext,
    val servers: Value<List<ServerProfile>>,
    val addServer: () -> Result<Unit>,
    val onServerSelected: (serverUuid: String) -> Unit
) : ComponentContext by componentContext {
}
