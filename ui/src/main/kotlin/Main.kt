@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.rememberWindowState
import arkascendedservermanager.ui.generated.resources.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import eu.wynq.arkascendedservermanager.core.db.repositories.SettingsRepository.createDefaultSettings
import eu.wynq.arkascendedservermanager.core.db.DatabaseHelper.connect
import eu.wynq.arkascendedservermanager.core.db.DatabaseHelper.migrate
import eu.wynq.arkascendedservermanager.core.db.repositories.SettingsRepository
import eu.wynq.arkascendedservermanager.core.support.LoggerConfigurator.configureLogging
import eu.wynq.arkascendedservermanager.core.support.PathHelper.getLogFilePath
import eu.wynq.arkascendedservermanager.core.support.SteamCMDHelper
import eu.wynq.arkascendedservermanager.ui.features.root.RootComponent
import eu.wynq.arkascendedservermanager.ui.features.root.RootScreen
import eu.wynq.arkascendedservermanager.ui.stores.ServersStore
import eu.wynq.arkascendedservermanager.ui.stores.ServersStoreImpl
import eu.wynq.arkascendedservermanager.ui.stores.SettingsStore
import eu.wynq.arkascendedservermanager.ui.stores.SettingsStoreImpl
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils.buildComponentStyling
import eu.wynq.arkascendedservermanager.ui.theme.ThemeUtils.buildThemeDefinition
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedDialog
import io.github.kdroidfilter.nucleus.window.jewel.JewelDecoratedWindow
import io.github.kdroidfilter.nucleus.window.jewel.JewelDialogTitleBar
import io.github.kdroidfilter.nucleus.window.jewel.JewelTitleBar
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.HorizontalProgressBar
import org.jetbrains.jewel.ui.component.InlineErrorBanner
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.theme.inlineBannerStyle
import org.jetbrains.jewel.window.newFullscreenControls
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import kotlin.system.measureTimeMillis

private val startupLogger = KotlinLogging.logger {}

private sealed interface StartupState {
    data class Starting(
        val step: StringResource,
        val percent: Int,
    ) : StartupState

    data class Ready(val rootComponent: RootComponent) : StartupState
    data class Failed(val throwable: Throwable) : StartupState
}

private suspend fun runStartupSequence(
    lifecycle: LifecycleRegistry,
    onProgress: (StartupState.Starting) -> Unit,
): StartupState.Ready {
    onProgress(StartupState.Starting(step = Res.string.startup_step_configuring_logging, percent = 20))
    val loggingMs = withContext(Dispatchers.IO) {
        measureTimeMillis { configureLogging(getLogFilePath()) }
    }

    onProgress(StartupState.Starting(step = Res.string.startup_step_migrating_database, percent = 40))
    val migrateMs = withContext(Dispatchers.IO) {
        measureTimeMillis { migrate() }
    }

    onProgress(StartupState.Starting(step = Res.string.startup_step_connecting_database, percent = 60))
    val connectMs = withContext(Dispatchers.IO) {
        measureTimeMillis { connect(); createDefaultSettings() }
    }

    onProgress(StartupState.Starting(step = Res.string.startup_step_steamcmd, percent = 80))
    val steamCmdMs = withContext(Dispatchers.IO) {
        measureTimeMillis {
            val settings = SettingsRepository.getSettings().getOrThrow()
            val helper = SteamCMDHelper(settings)
            helper.installSteamCMD().getOrThrow()
        }
    }

    startupLogger.info {
        "Startup DB timings: configureLogging=${loggingMs}ms, migrate=${migrateMs}ms, steamcmd=${steamCmdMs}ms, connect=${connectMs}ms"
    }

    onProgress(StartupState.Starting(step = Res.string.startup_step_initializing_ui, percent = 95))

    return StartupState.Ready(RootComponent(DefaultComponentContext(lifecycle)))
}

@Composable
private fun StartupDialogFrame(
    title: String,
    state: androidx.compose.ui.window.DialogState,
    onCloseRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    JewelDecoratedDialog(
        onCloseRequest = onCloseRequest,
        title = title,
        state = state,
        resizable = false,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JewelTheme.globalColors.panelBackground),
        ) {
            JewelDialogTitleBar {
                Text(title)
            }
            content()
        }
    }
}

@Composable
private fun StartupProgressDialog(
    state: StartupState.Starting,
    onCloseRequest: () -> Unit,
) {
    val startupDialogState = rememberDialogState(size = DpSize(460.dp, 120.dp))
    val title = stringResource(Res.string.startup_dialog_title)

    StartupDialogFrame(
        title = title,
        state = startupDialogState,
        onCloseRequest = onCloseRequest,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("${state.percent}% - ${stringResource(state.step)}")
                HorizontalProgressBar(
                    modifier = Modifier.fillMaxWidth(),
                    progress = state.percent.toFloat() / 100f,
                )
            }
        }
    }
}

@Composable
private fun StartupErrorDialog(
    throwable: Throwable,
    onCloseRequest: () -> Unit,
) {
    val errorDialogState = rememberDialogState(size = DpSize(620.dp, 180.dp))
    val title = stringResource(Res.string.error_startup_title)
    val bannerText = throwable.message ?: stringResource(Res.string.error_unknown)

    StartupDialogFrame(
        title = title,
        state = errorDialogState,
        onCloseRequest = onCloseRequest,
    ) {
        InlineErrorBanner(
            modifier = Modifier.fillMaxSize(),
            style = JewelTheme.inlineBannerStyle.error,
            title = title,
            text = bannerText,
        )
    }
}

@Composable
private fun MainWindow(
    windowState: androidx.compose.ui.window.WindowState,
    rootComponent: RootComponent,
    exitApplication: () -> Unit,
) {
    JewelDecoratedWindow(
        state = windowState,
        onCloseRequest = { exitApplication()},
        title = stringResource(Res.string.app_short_name),
    ) {
        Column(Modifier.fillMaxSize().background(JewelTheme.globalColors.panelBackground)) {
            JewelTitleBar(
                Modifier.newFullscreenControls(),
                gradientStartColor = ThemeUtils.titleBarGradientColor(),
            ) {
                Row(Modifier.align(Alignment.Start).padding(start = 8.dp)) {
                    Text(stringResource(Res.string.app_name))
                }
            }

            RootScreen(rootComponent)
        }
    }
}

fun main() {
    application {
        val lifecycle = remember { LifecycleRegistry() }
        val windowState = rememberWindowState()
        var startupState by remember {
            mutableStateOf<StartupState>(
                StartupState.Starting(step = Res.string.startup_step_preparing, percent = 0)
            )
        }
        val themeDefinition = buildThemeDefinition()
        val styling = buildComponentStyling()

        LaunchedEffect(Unit) {
            startupState = try {
                runStartupSequence(lifecycle) { startupState = it }
            } catch (t: Throwable) {
                StartupState.Failed(t)
            }
        }

        IntUiTheme(theme = themeDefinition, styling = styling) {
            when (val currentState = startupState) {
                is StartupState.Starting -> StartupProgressDialog(
                    state = currentState,
                    onCloseRequest = ::exitApplication,
                )

                is StartupState.Ready -> {
                    val appModule = module {
                        single<ServersStore> { ServersStoreImpl() }
                        single<SettingsStore> { SettingsStoreImpl() }
                    }

                    // Resolve DI only after startup is complete
                    KoinApplication(application = {
                        modules(appModule)
                    }) {
                        LifecycleController(lifecycle, windowState)
                        MainWindow(windowState = windowState, rootComponent = currentState.rootComponent, ::exitApplication)
                    }
                }

                is StartupState.Failed -> StartupErrorDialog(
                    throwable = currentState.throwable,
                    onCloseRequest = ::exitApplication,
                )
            }
        }
    }
}