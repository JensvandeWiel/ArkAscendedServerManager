package eu.wynq.arkascendedservermanager.core

import Failed
import Error
import Status
import SteamCMD
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.support.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.file.Path

sealed class InstallStatus {
    fun isInstalling() = !(this is Idle || this is InstallDone || this is InstallError)
}
class Idle : InstallStatus()
class Preparing : InstallStatus()
class InstallingAPI : InstallStatus()
class InstallDone : InstallStatus()
class InstallError(val error: String) : InstallStatus()
data class InstallingGame(val status: Status) : InstallStatus()


object InstallManager {

    fun isInstalled(server: Server): Boolean {
        val installationPath = Path
            .of(server.installationLocation, Constants.SERVER_BINARY_PATH)
            .resolve("ArkAscendedServer.exe")
        return installationPath.toFile().exists()
    }

    fun install(server: Server, steamCMD: SteamCMD, validate: Boolean = true): Flow<InstallStatus> = flow {
        var failed = false

        installServer(server, steamCMD, validate).collect { status ->
            when (status) {
                is Failed -> {
                    emit(InstallError(status.error.message))
                    failed = true
                }
                is Error -> {
                    emit(InstallError("SteamCMD exited with code: ${status.exitCode}"))
                    failed = true
                }
                else -> {
                    emit(InstallingGame(status))
                }
            }

            if (failed) return@collect
        }

        if (failed) return@flow

        emit(InstallingAPI())
        try {
            installAPI()
            emit(InstallDone())
        } catch (e: Exception) {
            emit(InstallError("API Installation failed: ${e.message}"))
        }
    }

    private suspend fun installAPI() {

    }

    private fun installServer(server: Server, steamCMD: SteamCMD, validate: Boolean = true): Flow<Status> =
        steamCMD.runAsFlow(listOf(
            "force_install_dir ${server.installationLocation}",
            "login anonymous",
            "app_update ${Constants.ARK_APP_ID}${if (validate) " validate" else ""}",
            "quit"
        ))
}