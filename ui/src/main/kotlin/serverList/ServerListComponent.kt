package ui.serverList

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import server.ServerConfig

class ServerListComponent(
    componentContext: ComponentContext,
    val servers: Value<List<ServerConfig>>,
    val addServer: () -> Result<Unit>
) : ComponentContext by componentContext {
}
