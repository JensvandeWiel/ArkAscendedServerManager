@file:OptIn(ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.ui.features.root

import arkascendedservermanager.ui.generated.resources.Res
import arkascendedservermanager.ui.generated.resources.page_info
import arkascendedservermanager.ui.generated.resources.page_servers
import arkascendedservermanager.ui.generated.resources.page_settings
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import eu.wynq.arkascendedservermanager.ui.features.info.InfoComponent
import eu.wynq.arkascendedservermanager.ui.features.info.InfoScreen
import eu.wynq.arkascendedservermanager.ui.features.servers.ServersComponent
import eu.wynq.arkascendedservermanager.ui.features.servers.ServersScreen
import eu.wynq.arkascendedservermanager.ui.features.settings.SettingsComponent
import eu.wynq.arkascendedservermanager.ui.features.settings.SettingsScreen
import eu.wynq.arkascendedservermanager.ui.features.server.ServerComponent
import kotlin.toString
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RootComponent(context: ComponentContext) : ComponentContext by context {
    sealed class Child {
        class Servers(
            val component: ServersComponent
        ) : Child()
        class Settings(
            val component: SettingsComponent
        ) : Child()
        class Info(
            val component: InfoComponent
        ) : Child()
        class Server(
            val component: ServerComponent
        ) : Child()
    }

    // 4. The Type-Safe Configuration
    @Serializable
    sealed class Config {
        @Serializable
        object Servers : Config()
        @Serializable
        object Settings : Config()
        @Serializable
        object Info : Config()
        @Serializable
        data class Server(val serverId: String) : Config()
    }

    val pages: List<Page<out Child>> = listOf(
        Page<Child.Servers>(
            title = Res.string.page_servers,
            config = Config.Servers,
            iconKey = AllIconsKeys.Toolwindows.ToolWindowServer,
            section = PageSection.TOP,
            content = { child -> ServersScreen(child.component) }
        ),
        Page<Child.Info>(
            title = Res.string.page_info,
            config = Config.Info,
            iconKey = AllIconsKeys.General.ShowInfos,
            section = PageSection.BOTTOM,
            content = { child -> InfoScreen(child.component) }
        ),
        Page<Child.Settings>(
            title = Res.string.page_settings,
            config = Config.Settings,
            iconKey = AllIconsKeys.General.Settings,
            section = PageSection.BOTTOM,
            content = { child -> SettingsScreen(child.component) }
        ),
    )

    val topPages = pages.filter { it.section == PageSection.TOP }
    val bottomPages = pages.filter { it.section == PageSection.BOTTOM }

    private val pagesByConfig = pages.associateBy { it.config }

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Info,
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, context: ComponentContext): Child =
        when (config) {
            is Config.Servers -> Child.Servers(
                component = ServersComponent(
                    componentContext = context,
                    onOpenServer = ::openServer,
                )
            )
            is Config.Settings -> Child.Settings(
                component = SettingsComponent(context)
            )
            is Config.Info -> Child.Info(
                component = InfoComponent(context)
            )
            is Config.Server -> {
                val parsedServerId = try {
                    Uuid.parse(config.serverId)
                } catch (_: IllegalArgumentException) {
                    return Child.Info(component = InfoComponent(context))
                }

                Child.Server(
                    component = ServerComponent(
                        componentContext = context,
                        serverId = parsedServerId,
                        onServerDeleted = ::onServerDeleted
                    )
                )
            }
        }

    fun pageFor(config: Config): Page<out Child>? = pagesByConfig[config]

    fun navigateTo(config: Config) = navigation.bringToFront(config)

    fun onServerDeleted(uuid: Uuid) {
        navigation.navigate { stack ->
            stack.filterNot { it is Config.Server && it.serverId == uuid.toString() }
        }
        navigateTo(Config.Servers)
    }

    fun openServer(serverId: Uuid) = navigation.bringToFront(Config.Server(serverId.toString()))

    fun currentActive(config: Config) = stack.value.active.configuration == config
}