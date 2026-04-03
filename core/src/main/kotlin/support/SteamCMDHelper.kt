package eu.wynq.arkascendedservermanager.core.support

import Error
import Failed
import Installer
import SteamCMD
import eu.wynq.arkascendedservermanager.core.db.models.Settings
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

class SteamCMDHelper(val settings: Settings) {
    fun installSteamCMD(): Result<Boolean> {
        val installer = Installer(Path.of(settings.steamcmdPath))

        return installer.install()
    }

    fun updateSteamCMD(): Result<Boolean> = runBlocking {
        var error: IllegalStateException? = null

        SteamCMD(Path.of(settings.steamcmdPath)).runAsFlow(
            listOf(
                "login anonymous"
            )
        ).collect { status ->
            when (status) {
                is Failed -> error = IllegalStateException("SteamCMD failed: ${status.error.message}")
                is Error -> error = IllegalStateException("SteamCMD failed with code ${status.exitCode}")
                else -> Unit
            }
        }
        error?.let { Result.failure(it) } ?: Result.success(true)
    }
}