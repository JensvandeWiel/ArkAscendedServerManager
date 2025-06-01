@file:OptIn(ExperimentalUuidApi::class)

package ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.push
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

    // Store created server components to preserve their state
    private val serverComponents = mutableMapOf<String, ServerComponent>()

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
        val serverInStack = stack.value.items.find { item ->
            val config = item.configuration
            config is Config.Server && config.serverUuid == serverUuid
        }
        if (serverInStack == null) {
            navigation.push(Config.Server(serverUuid))
        } else {
            navigation.bringToFront(Config.Server(serverUuid))
        }
    }

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ServerList,
            handleBackButton = true,
            key = "root_stack",
            childFactory = ::createChild
        )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): Child =
        when (config) {
            is Config.Settings -> Child.Settings(
                component = SettingsComponent(componentContext)
            )
            is Config.ServerList -> Child.ServerList(
                component = ServerListComponent(componentContext, servers, ::addServer, ::navigateToServer)
            )
            is Config.Server -> {
                val existingComponent = serverComponents[config.serverUuid]
                existingComponent?.let {
                    return@createChild Child.Server(serverUuid = config.serverUuid, component = it)
                }

                createAndStoreServerComponent(componentContext, config.serverUuid)
            }
        }

    /**
     * Creates a new ServerComponent, stores it in the serverComponents map and returns a Child.Server
     */
    private fun createAndStoreServerComponent(
        componentContext: ComponentContext,
        serverUuid: String
    ): Child.Server {
        val serverValue = servers.map {
            it.find { server -> server.uuid.toString() == serverUuid }
                ?: error("Server not found in the list")
        }

        val onUpdateServer: (ServerProfile) -> Result<Unit> = { updatedServer ->
            val result = ProfileLoader.updateProfile(profile = updatedServer)
            if (result.isSuccess) {
                servers.value = servers.value.map { if (it.uuid == updatedServer.uuid) updatedServer else it }
            }
            result
        }

        // Create new components and store it
        val newComponent = ServerComponent(componentContext, serverValue, onUpdateServer)
        serverComponents[serverUuid] = newComponent

        return Child.Server(serverUuid = serverUuid, component = newComponent)
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
                if (this === other) return true
                if (other !is Server) return false
                return serverUuid == other.serverUuid
            }

            override fun hashCode(): Int {
                return serverUuid.hashCode()
            }
        }
    }
}
