package ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ui.main.MainComponent
import ui.settings.SettingsComponent

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    sealed class Child {
        class Main(
            val component: MainComponent
        ) : Child()

        class Settings(
            val component: SettingsComponent
        ) : Child()
    }

    fun navigateToMain() {
        navigation.bringToFront(Config.Main)
    }

    fun isMainActive(): Boolean {
        return stack.value.active.configuration == Config.Main
    }

    fun navigateToSettings() {
        navigation.bringToFront(Config.Settings)
    }

    fun isSettingsActive(): Boolean {
        return stack.value.active.configuration == Config.Settings
    }


    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (config) {
            is Config.Main -> RootComponent.Child.Main(
                component = MainComponent(componentContext)
            )
            is Config.Settings -> RootComponent.Child.Settings(
                component = SettingsComponent(componentContext)
            )
        }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Main : Config()
        @Serializable
        data object Settings : Config()
    }
}
