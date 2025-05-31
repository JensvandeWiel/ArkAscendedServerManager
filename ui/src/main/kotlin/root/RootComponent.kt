package ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import server.ServerConfig
import server.ServerLoader
import ui.settings.SettingsComponent
import ui.serverList.ServerListComponent

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    val servers: MutableValue<List<ServerConfig>> = MutableValue<List<ServerConfig>>(
        ServerLoader.loadServers().getOrNull() ?: emptyList()
    )

    // Functions to manage servers
    fun addServer(): Result<Unit> {
        val newServer = ServerLoader.generateNewServer()
        servers.value = servers.value + newServer
        return ServerLoader.addServer(serverConfig = newServer)
    }

    sealed class Child {
        class ServerList(
            val component: ServerListComponent
        ) : Child()
        class Settings(
            val component: SettingsComponent
        ) : Child()
    }

    fun navigateToSettings() {
        navigation.bringToFront(Config.Settings)
    }

    fun isSettingsActive(): Boolean {
        return stack.value.active.configuration == Config.Settings
    }

    fun navigateToServerList() {
        navigation.bringToFront(Config.ServerList)
    }

    fun isServerListActive(): Boolean {
        return stack.value.active.configuration == Config.ServerList
    }


    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ServerList,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (config) {
            is Config.Settings -> RootComponent.Child.Settings(
                component = SettingsComponent(componentContext)
            )
            is Config.ServerList -> RootComponent.Child.ServerList(
                component = ServerListComponent(componentContext, servers, ::addServer)
            )
        }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Settings : Config()
        @Serializable
        data object ServerList : Config()
    }
}
