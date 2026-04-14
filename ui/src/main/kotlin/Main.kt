@file:OptIn(ExperimentalFoundationApi::class)

package eu.wynq.arkascendedservermanager.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
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
import eu.wynq.arkascendedservermanager.ui.helpers.AppBuildInfo
import eu.wynq.arkascendedservermanager.ui.stores.InstallStore
import eu.wynq.arkascendedservermanager.ui.stores.PowerStore
import eu.wynq.arkascendedservermanager.ui.stores.PowerStoreImpl
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
import io.github.kdroidfilter.nucleus.updater.NucleusUpdater
import io.github.kdroidfilter.nucleus.updater.UpdateResult
import io.github.kdroidfilter.nucleus.updater.provider.GitHubProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.HorizontalProgressBar
import org.jetbrains.jewel.ui.component.InlineErrorBanner
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.theme.inlineBannerStyle
import org.jetbrains.jewel.window.newFullscreenControls
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import kotlin.system.measureTimeMillis

private val startupLogger = KotlinLogging.logger {}
private const val UPDATE_OWNER = "JensvandeWiel"
private const val UPDATE_REPO = "ArkAscendedServerManager"

private enum class UpdaterModeOverride {
    AVAILABLE,
    NOT_AVAILABLE,
    ERROR,
}

private fun parseUpdaterModeOverride(): UpdaterModeOverride? {
    val raw = System.getProperty("updater.mode")?.trim()?.lowercase() ?: return null
    return when (raw) {
        "available" -> UpdaterModeOverride.AVAILABLE
        "not-available", "not_available", "notavailable" -> UpdaterModeOverride.NOT_AVAILABLE
        "error" -> UpdaterModeOverride.ERROR
        else -> {
            startupLogger.warn {
                "Unknown updater.mode='$raw'. Supported values: available, not-available, error. Falling back to live updater."
            }
            null
        }
    }
}

private sealed interface StartupState {
    data class Starting(
        val step: StringResource,
        val percent: Int,
    ) : StartupState

    data class UpdateAvailable(
        val latestVersion: String,
        val update: UpdateResult.Available? = null,
    ) : StartupState
    data class DownloadingUpdate(val version: String, val percent: Int) : StartupState
    data class Ready(val rootComponent: RootComponent) : StartupState
    data class Failed(val throwable: Throwable) : StartupState
}

private fun buildUpdater(): NucleusUpdater = NucleusUpdater {
    provider = GitHubProvider(owner = UPDATE_OWNER, repo = UPDATE_REPO)
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
            helper.updateSteamCMD().getOrThrow()
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
                Text(stringResource(Res.string.startup_progress_format, state.percent, stringResource(state.step)))
                HorizontalProgressBar(
                    modifier = Modifier.fillMaxWidth(),
                    progress = state.percent.toFloat() / 100f,
                )
            }
        }
    }
}

@Composable
private fun UpdateAvailableDialog(
    state: StartupState.UpdateAvailable,
    onInstall: () -> Unit,
    onSkip: () -> Unit,
) {
    val dialogState = rememberDialogState(size = DpSize(460.dp, 190.dp))
    val title = stringResource(Res.string.startup_update_available_title)
    val latestVersion = state.latestVersion

    StartupDialogFrame(
        title = title,
        state = dialogState,
        onCloseRequest = onSkip,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                stringResource(
                    Res.string.startup_update_available_body,
                    AppBuildInfo.version,
                    latestVersion,
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                DefaultButton(onClick = onSkip) {
                    Text(stringResource(Res.string.action_skip_for_now))
                }
                Spacer(Modifier.width(8.dp))
                DefaultButton(onClick = onInstall) {
                    Text(stringResource(Res.string.action_update_now))
                }
            }
        }
    }
}

@Composable
private fun DownloadingUpdateDialog(
    state: StartupState.DownloadingUpdate,
    onCloseRequest: () -> Unit,
) {
    val dialogState = rememberDialogState(size = DpSize(520.dp, 150.dp))
    val title = stringResource(Res.string.startup_update_downloading_title)

    StartupDialogFrame(
        title = title,
        state = dialogState,
        onCloseRequest = onCloseRequest,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(stringResource(Res.string.startup_update_downloading_format, state.version, state.percent))
            HorizontalProgressBar(
                modifier = Modifier.fillMaxWidth(),
                progress = state.percent.toFloat() / 100f,
            )
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

val LocalAppWindow = staticCompositionLocalOf<ComposeWindow?> {
    null
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
        icon = painterResource(Res.drawable.aasm)
    ) {
        CompositionLocalProvider(LocalAppWindow provides window) {
            Column(Modifier.fillMaxSize().background(JewelTheme.globalColors.panelBackground)) {
                JewelTitleBar(
                    Modifier.newFullscreenControls(),
                    gradientStartColor = ThemeUtils.titleBarGradientColor(),
                ) {
                    Row(Modifier.align(Alignment.Start).padding(start = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(Res.drawable.aasm), contentDescription = null, modifier = Modifier.size(30.dp))
                        Text(stringResource(Res.string.app_name))
                    }
                }

                RootScreen(rootComponent)
            }
        }
    }
}

fun main() {
    val previousUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        startupLogger.error(throwable) { "Uncaught UI exception on thread ${thread.name}" }
        previousUncaughtExceptionHandler?.uncaughtException(thread, throwable)
    }

    application {
        val lifecycle = remember { LifecycleRegistry() }
        val windowState = rememberWindowState()
        val updater = remember { buildUpdater() }
        val updaterModeOverride = remember { parseUpdaterModeOverride() }
        val scope = rememberCoroutineScope()
        var startupState by remember {
            mutableStateOf<StartupState>(
                StartupState.Starting(step = Res.string.startup_step_preparing, percent = 0)
            )
        }
        val themeDefinition = buildThemeDefinition()
        val styling = buildComponentStyling()

        LaunchedEffect(Unit) {
            startupState = try {
                startupState = StartupState.Starting(step = Res.string.startup_step_checking_updates, percent = 10)
                when (updaterModeOverride) {
                    UpdaterModeOverride.AVAILABLE -> {
                        startupLogger.info { "Using updater.mode=available (dev override)" }
                        StartupState.UpdateAvailable(
                            latestVersion = "test-${AppBuildInfo.version}",
                            update = null,
                        )
                    }

                    UpdaterModeOverride.NOT_AVAILABLE -> {
                        startupLogger.info { "Using updater.mode=not-available (dev override)" }
                        runStartupSequence(lifecycle) { startupState = it }
                    }

                    UpdaterModeOverride.ERROR -> {
                        startupLogger.info { "Using updater.mode=error (dev override)" }
                        startupLogger.warn { "Simulated updater error, continuing startup" }
                        runStartupSequence(lifecycle) { startupState = it }
                    }

                    null -> when (val updateResult = withContext(Dispatchers.IO) { updater.checkForUpdates() }) {
                        is UpdateResult.Available -> StartupState.UpdateAvailable(
                            latestVersion = updateResult.info.version,
                            update = updateResult,
                        )

                        is UpdateResult.NotAvailable -> runStartupSequence(lifecycle) { startupState = it }
                        is UpdateResult.Error -> {
                            startupLogger.warn(updateResult.exception) { "Update check failed, continuing startup" }
                            runStartupSequence(lifecycle) { startupState = it }
                        }
                    }
                }
            } catch (t: Throwable) {
                startupLogger.error(t) { "Startup failed" }
                StartupState.Failed(t)
            }
        }

        IntUiTheme(theme = themeDefinition, styling = styling) {
            when (val currentState = startupState) {
                is StartupState.Starting -> StartupProgressDialog(
                    state = currentState,
                    onCloseRequest = ::exitApplication,
                )

                is StartupState.UpdateAvailable -> UpdateAvailableDialog(
                    state = currentState,
                    onInstall = {
                        scope.launch {
                            startupState = try {
                                val updateResult = currentState.update
                                if (updateResult == null) {
                                    startupLogger.info {
                                        "Install clicked while updater.mode=available is active; skipping install and continuing startup"
                                    }
                                    runStartupSequence(lifecycle) { startupState = it }
                                }

                                else {
                                    val version = currentState.latestVersion
                                    withContext(Dispatchers.IO) {
                                        var installed = false
                                        updater.downloadUpdate(updateResult.info).collect { progress ->
                                            startupState = StartupState.DownloadingUpdate(
                                                version = version,
                                                percent = progress.percent.toInt().coerceIn(0, 100),
                                            )
                                            if (!installed && progress.file != null) {
                                                installed = true
                                                updater.installAndRestart(progress.file!!)
                                            }
                                        }
                                    }

                                    throw IllegalStateException("Update install did not start")
                                }
                            } catch (t: Throwable) {
                                startupLogger.error(t) { "Update install failed" }
                                StartupState.Failed(t)
                            }
                        }
                    },
                    onSkip = {
                        scope.launch {
                            startupState = try {
                                runStartupSequence(lifecycle) { startupState = it }
                            } catch (t: Throwable) {
                                startupLogger.error(t) { "Startup resume after update skip failed" }
                                StartupState.Failed(t)
                            }
                        }
                    },
                )

                is StartupState.DownloadingUpdate -> DownloadingUpdateDialog(
                    state = currentState,
                    onCloseRequest = ::exitApplication,
                )

                is StartupState.Ready -> {
                    val appModule = module {
                        single {
                            val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                                startupLogger.error(throwable) { "Unhandled UI coroutine failure" }
                            }
                            CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)
                        }
                        single<ServersStore> { ServersStoreImpl() }
                        single<SettingsStore> { SettingsStoreImpl() }
                        single { InstallStore(get(), get()) }
                        single<PowerStore> { PowerStoreImpl(get()) }
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