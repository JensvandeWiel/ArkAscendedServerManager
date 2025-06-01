@file:OptIn(ExperimentalUuidApi::class)

package server

import Installer
import Status
import SteamCMD
import io.github.oshai.kotlinlogging.KotlinLogging
import settings.SettingsHelper
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi

class ServerManager(private val _profile: ServerProfile) {
    private val logger = KotlinLogging.logger("ServerManager (${_profile.uuid})")
    fun isInstalled(): Boolean {
        // Check if the installation directory exists
        val installationPath = Path.of(_profile.installationLocation).resolve("ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe")
        return installationPath.toFile().exists()
    }

    suspend fun install(onStatusUpdate: (status: Status) -> Unit): Result<Unit> {
        if (ServerManager.installerRunning()) {
            return Result.failure(Exception("Installation is already running, please wait until it completes."))
        }
        ServerManager.setInstalling(true)
        // Get steamcmd path from the global settings
        val steamcmdPath = SettingsHelper().getSettings().getOrElse {
            return Result.failure(it)
        }.steamCmdPath

        val steamInstallResult = Installer(Path.of(steamcmdPath)).install()
        if (steamInstallResult.isFailure) {
            return Result.failure(steamInstallResult.exceptionOrNull() ?: Exception("Unknown error during installation"))
        }

        val statusFlow = SteamCMD(Path.of(steamcmdPath)).runAsFlow(
            listOf(
                "force_install_dir ${_profile.installationLocation}",
                "login anonymous",
                "app_update 2430930 validate",
                "quit"
            )
        )

        statusFlow.collect { status ->
            onStatusUpdate(status)
            logger.info { "Install/Update status update on ${_profile.profileName} (${_profile.uuid}): $status" }
        }
        ServerManager.setInstalling(false)
        return Result.success(Unit)
    }

    companion object {
        private var installing = false;
        fun installerRunning(): Boolean {
            return installing
        }

        private fun setInstalling(value: Boolean) {
            installing = value
        }
    }
}