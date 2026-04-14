package eu.wynq.arkascendedservermanager.core.managers

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.rcon.ArkRconClient
import eu.wynq.arkascendedservermanager.core.rcon.ArkRconConfig
import eu.wynq.arkascendedservermanager.core.support.Constants
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import oshi.ffm.SystemInfo
import oshi.software.os.OSProcess
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class PowerState {
    Running,
    Starting,
    Stopped,
    Unknown,
    Stopping,
}

object PowerManager {
    suspend fun getPowerState(server: Server, oldState: PowerState = PowerState.Unknown): PowerState = withContext(Dispatchers.IO) {
        val process = getProcess(server).getOrElse {
            return@withContext PowerState.Unknown
        }

        if (process == null) {
            return@withContext PowerState.Stopped
        }

        if (isAvailable(server)) {
            PowerState.Running
        } else {
            if (oldState == PowerState.Stopping) PowerState.Stopping else PowerState.Starting
        }
    }

    fun pollPowerState(
        server: Server,
        interval: Duration = 10.seconds,
        oldStateProvider: () -> PowerState = { PowerState.Unknown },
    ): Flow<PowerState> = flow {
        while (true) {
            emit(getPowerState(server, oldStateProvider()))
            delay(interval)
        }
    }

    private fun getServerRconClient(server: Server): ArkRconClient = ArkRconClient(
        ArkRconConfig(
            "127.0.0.1",
            server.settings.administration.rconPort,
            server.settings.administration.adminPassword
        )
    )

    private suspend fun isAvailable(server: Server): Boolean = withContext(Dispatchers.IO) {
        val client = getServerRconClient(server)

        client.use {
            try {
                it.connectAndAuthenticate()
                val response = it.execute("ListPlayers")
                return@withContext response.output.isNotEmpty()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                return@withContext false
            }
        }
    }

    private fun getProcess(server: Server, getWrapper: Boolean = true): Result<OSProcess?> {
        return try {
            val systemInfo = SystemInfo()
            val pathToCheck = Path.of(
                server.installationLocation,
                Constants.SERVER_BINARY_PATH,
                if (server.asaApi && getWrapper) Constants.OVERSEER_EXECUTABLE_NAME else Constants.SERVER_EXECUTABLE_NAME
            ).toString()
            val processes = systemInfo.operatingSystem.processes
            val process = processes.find {
                it.path.equals(
                    pathToCheck,
                    ignoreCase = true
                )
            }
            Result.success(process)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun startServer(server: Server): Result<Unit> {
        return try {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val startupScriptPath = Path.of(server.installationLocation, Constants.STARTUP_SCRIPT_PATH)
                    val processBuilder = ProcessBuilder(startupScriptPath.toString())
                    processBuilder.directory(Path.of(server.installationLocation, Constants.SERVER_BINARY_PATH).toFile())
                    processBuilder.start()
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stopServer(server: Server): Result<Unit> = runCatching {
        getServerRconClient(server).use {
            it.connectAndAuthenticate()
            it.execute("DoExit")
        }
    }

    fun killServer(server: Server): Result<Unit> {
        val process = getProcess(server).getOrElse {
            return Result.failure(IllegalStateException("Failed to get process"))
        } ?: return Result.failure(IllegalStateException("Server process not found"))

        return try {
            runBlocking {
                withContext(Dispatchers.IO) {
                    ProcessBuilder("taskkill", "/PID", process.processID.toString(), "/F").start()
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}