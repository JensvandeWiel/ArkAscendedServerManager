package server

import oshi.SystemInfo
import java.nio.file.Path

class PowerManager(private val profile: ServerProfile) {
    /**
     * Checks if the server is currently running by looking for the process and matching the path.
     * */
    fun isRunning(): Result<Boolean> {
        val systemInfo = SystemInfo()
        val processes = systemInfo.operatingSystem.processes
        return try {
            val isRunning = processes.any { it.path.equals(Path.of(profile.installationLocation).resolve("ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe").toString(), ignoreCase = true) }
            Result.success(isRunning)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}