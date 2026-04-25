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
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class PowerState {
    Running,
    Starting,
    Stopped,
    Unknown,
    Stopping,
    Crashed,
}

@OptIn(kotlin.uuid.ExperimentalUuidApi::class)
object PowerManager {

    suspend fun getPowerState(server: Server, oldState: PowerState = PowerState.Unknown, interval: Duration = 10.seconds): PowerState = withContext(Dispatchers.IO) {
        val process = getProcess(server).getOrElse {
            return@withContext PowerState.Unknown
        }

        if (process == null) {
            if (oldState == PowerState.Running || oldState == PowerState.Starting) {
                if (checkCrash(server, interval)) {
                    return@withContext PowerState.Crashed
                }
            }
            return@withContext PowerState.Stopped
        }

        if (isAvailable(server)) {
            PowerState.Running
        } else {
            if (oldState == PowerState.Stopping) PowerState.Stopping else PowerState.Starting
        }
    }
    private fun checkCrash(server: Server, interval: Duration): Boolean {
        val crashDir = Path.of(server.installationLocation, "ShooterGame", "Saved", "Crashes")
        if (!Files.exists(crashDir) || !Files.isDirectory(crashDir)) return false
        val now = System.currentTimeMillis()
        val threshold = now - interval.inWholeMilliseconds
        return Files.list(crashDir).use { stream ->
            stream.filter { Files.isDirectory(it) }.anyMatch { path ->
                try {
                    val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)
                    attrs.creationTime().toMillis() >= threshold
                } catch (_: Exception) {
                    false
                }
            }
        }
    }

    fun pollPowerState(
        server: Server,
        interval: Duration = 10.seconds,
        oldStateProvider: () -> PowerState = { PowerState.Unknown },
    ): Flow<PowerState> = flow {
        var lastState = oldStateProvider()
        while (true) {
            val state = getPowerState(server, lastState, interval)
            emit(state)
            lastState = state
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