@file:OptIn(ExperimentalUuidApi::class)

package ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlinx.serialization.Serializable
import server.ServerProfile
import server.ProfileLoader
import ui.server.ServerComponent
import ui.settings.SettingsComponent
import ui.serverList.ServerListComponent
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    val servers: MutableValue<List<ServerProfile>> = MutableValue<List<ServerProfile>>(
        ProfileLoader.loadProfiles().getOrNull() ?: emptyList()
    )

    // Functions to manage servers
    fun addServer(): Result<Unit> {
        val newServer = ProfileLoader.generateNewProfile()
        servers.value = servers.value + newServer
        return ProfileLoader.addProfile(profile = newServer)
    }

    sealed class Child {
        class ServerList(
            val component: ServerListComponent
        ) : Child()
        class Settings(
            val component: SettingsComponent
        ) : Child()
        class Server(
            val serverUuid: String,
            val component: ServerComponent
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

    fun navigateToServer(serverUuid: String) {
        navigation.bringToFront(Config.Server(serverUuid))
    }

    fun isServerActive(serverUuid: Uuid): Boolean {
        return stack.value.active.configuration == Config.Server(serverUuid.toString())
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
                component = ServerListComponent(componentContext, servers, ::addServer, ::navigateToServer)
            )
            is Config.Server -> {
                val serverConfig = servers.value.find { it.uuid.toString() == config.serverUuid }
                    ?: error("Server not found")
                val serverValue = servers.map {
                    it.find { server -> server.uuid.toString() == config.serverUuid }
                        ?: error("Server not found in the list")
                }
                val onUpdateServer: (ServerProfile) -> Result<Unit> = { updatedServer ->
                    val result = ProfileLoader.updateProfile(profile = updatedServer)
                    if (result.isSuccess) {
                        servers.value = servers.value.map { if (it.uuid == updatedServer.uuid) updatedServer else it }
                    }
                    result
                }
                Child.Server(
                    serverUuid = config.serverUuid,
                    component = ServerComponent(componentContext, serverValue, onUpdateServer)
                )
            }
        }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Settings : Config()
        @Serializable
        data object ServerList : Config()
        @Serializable
        data class Server(val serverUuid: String) : Config() {
            override fun equals(other: Any?): Boolean {
                return other is Server && other.serverUuid == serverUuid
            }
            override fun hashCode(): Int {
                return serverUuid.hashCode()
            }
        }
    }
}
