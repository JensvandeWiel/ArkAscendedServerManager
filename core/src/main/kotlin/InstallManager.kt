package eu.wynq.arkascendedservermanager.core

import Error
import Failed
import Installed
import Status
import SteamCMD
import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.support.AsaApiInstallManager
import eu.wynq.arkascendedservermanager.core.support.Constants
import eu.wynq.arkascendedservermanager.corenative.CoreNative
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

                is Installed -> {
                    // Get and store server version
                    getAndStoreServerVersion(server)
                }

                else -> {
                    emit(InstallingGame(status))
                }
            }

            if (failed) return@collect
        }

        if (failed) return@flow

        if (server.asaApi) {
            emit(InstallingAPI())
            try {
                installAPI(server)
            } catch (e: Exception) {
                emit(InstallError("API Installation failed: ${e.message}"))
                return@flow
            }
        }

        createStartupScript(server).onFailure {
            emit(InstallError("Failed to create startup script: ${it.message}"))
            return@flow
        }
        emit(InstallDone())
    }

    private suspend fun installAPI(server: Server) {
        val latest = AsaApiInstallManager.getLatestRelease().getOrThrow()
        val currentVersion = AsaApiInstallManager.getApiVersion(server)
        if (currentVersion == latest.version) return
        AsaApiInstallManager.install(server, latest).getOrThrow()
    }

    fun getServerVersionFromExecutable(server: Server): String? {
        val serverExecutable =
            Path.of(server.installationLocation, Constants.SERVER_BINARY_PATH, Constants.SERVER_EXECUTABLE_NAME)
                .toFile()
        if (!serverExecutable.exists()) return null

        return CoreNative.getAsaVersion(serverExecutable.toPath())
    }

    fun getServerVersion(server: Server): String? {
        // Check if server version.txt file exists
        val versionFile = Path.of(server.installationLocation, Constants.SERVER_VERSION_PATH).toFile()
        if (versionFile.exists()) {
            return versionFile.readText()
        }

        val version = getServerVersionFromExecutable(server) ?: return null
        storeServerVersion(server, version)

        return version
    }

    fun createStartupScript(server: Server): Result<Unit> {
        val content = server.makeStartupScriptString()

        val scriptFile = Path.of(server.installationLocation, Constants.STARTUP_SCRIPT_PATH).normalize().toFile()
        scriptFile.writeText(content)
        scriptFile.setExecutable(true)

        return Result.success(Unit)
    }

    fun getAndStoreServerVersion(server: Server) {
        val version = getServerVersionFromExecutable(server) ?: return
        storeServerVersion(server, version)
    }

    fun storeServerVersion(server: Server, version: String) {
        // Create or update server version.txt file
        val versionFile = Path.of(server.installationLocation, Constants.SERVER_VERSION_PATH).toFile()
        versionFile.writeText(version)
    }

    private fun installServer(server: Server, steamCMD: SteamCMD, validate: Boolean = true): Flow<Status> =
        steamCMD.runAsFlow(
            listOf(
                "force_install_dir ${server.installationLocation}",
                "login anonymous",
                "app_update ${Constants.ARK_APP_ID}${if (validate) " validate" else ""}",
                "quit"
            )
        )
}