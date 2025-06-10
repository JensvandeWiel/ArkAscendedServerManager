package server

import oshi.SystemInfo
import oshi.software.os.OSProcess
import java.nio.file.Path

class PowerManager(private val profile: ServerProfile) {

    fun getProcess(): Result<OSProcess?> {
        val systemInfo = SystemInfo()
        val processes = systemInfo.operatingSystem.processes
        return try {
            val process = processes.find {
                it.path.equals(
                    Path.of(profile.installationLocation)
                        .resolve("ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe")
                        .toString(),
                    ignoreCase = true
                )
            }
            Result.success(process)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Checks if the server is currently running by looking for the process and matching the path.
     * */
    fun isRunning(): Result<Int?> {
        return try {
            val process = getProcess()
            if (process.isSuccess) {
                val osProcess = process.getOrNull()
                if (osProcess != null) {
                    Result.success(osProcess.processID)
                } else {
                    Result.success(null)
                }
            } else {
                Result.failure(process.exceptionOrNull() ?: Exception("Failed to retrieve process"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Starts the server using the generated startup script.
     * */
    fun startServer(): Result<Unit> {
        if (isRunning().getOrNull() != null) {
            return Result.failure(Exception("Server is already running."))
        }

        return try {
            val startupScript = profile.generateStartupScript()
            val processBuilder = ProcessBuilder("cmd.exe", "/c", startupScript)
            processBuilder.directory(Path.of(profile.installationLocation).toFile())
            processBuilder.start()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Stops the server by killing the process.
     * */
    fun stopServer(): Result<Unit> {
        val processResult = getProcess()
        if (processResult.isFailure) {
            return Result.failure(processResult.exceptionOrNull() ?: Exception("Failed to retrieve process"))
        }

        val osProcess = processResult.getOrNull()
        if (osProcess == null) {
            return Result.failure(Exception("Server is not running."))
        }

        return try {
            val result = ProcessBuilder("taskkill", "/F", "/PID", osProcess.processID.toString()).start()
            result.waitFor()
            if (result.exitValue() != 0) {
                return Result.failure(Exception("Failed to stop the server. Exit code: ${result.exitValue()}"))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}