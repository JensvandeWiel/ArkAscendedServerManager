@file:OptIn(ExperimentalUuidApi::class, ExperimentalAtomicApi::class)

package server

import Status
import SteamCMD
import Installer
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.isSuccess
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import settings.SettingsHelper
import java.io.File
import java.nio.file.Path
import java.util.zip.ZipFile
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.uuid.ExperimentalUuidApi

class InstallManager(private val profile: ServerProfile) {
    val binaryPath = "ShooterGame\\Binaries\\Win64"
    val arkId = "2088130"
    val profileBinaryPath: Path = Path.of(profile.installationLocation, binaryPath)
    private val logger = KotlinLogging.logger("InstallManager (${profile.uuid})")
    private val httpClient = HttpClient(CIO) {
        engine {
            requestTimeout = 1000 * 60 * 5
            endpoint {
                connectTimeout = 1000 * 60 * 5
                connectAttempts = 5
            }
        }
    }

    fun isInstalled(): Boolean {
        val installationPath =
            profileBinaryPath.resolve("ArkAscendedServer.exe")
        return installationPath.toFile().exists()
    }

    private suspend fun getLatestApiRelease(): Result<String> {
        if (!profileBinaryPath.toFile().exists()) {
            profileBinaryPath.toFile().mkdirs()
        }
        val url = "https://api.github.com/repos/ArkServerApi/AsaApi/releases/latest"
        return try {
            val response = httpClient.get(url)
            val body = response.bodyAsText()
            val json = Json.parseToJsonElement(body).jsonObject
            val tagName = json["tag_name"]?.jsonPrimitive?.content
            if (tagName != null) {
                Result.success(tagName)
            } else {
                Result.failure(Exception("tag_name not found in API response"))
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to fetch latest API release" }
            Result.failure(e)
        }
    }

    /**
     * Extract a zip file to the specified destination path
     */
    private fun extractZip(zipFile: File, destinationPath: Path): Result<Unit> {
        return try {
            ZipFile(zipFile).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    if (!entry.isDirectory) {
                        val outputFile = destinationPath.resolve(entry.name)
                        outputFile.parent.toFile().mkdirs()
                        zip.getInputStream(entry).use { input ->
                            outputFile.toFile().outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            logger.info { "Extracted zip successfully to $destinationPath" }
            Result.success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Error during zip extraction" }
            Result.failure(e)
        }
    }

    /**
     * Download a file from a URL to a local path
     */
    private suspend fun downloadFile(url: String, destinationPath: Path): Result<Unit> {
        return try {
            val response = httpClient.get(url)
            if (response.status.isSuccess()) {
                response.bodyAsChannel().copyTo(destinationPath.toFile().outputStream())
                logger.info { "Downloaded file successfully to $destinationPath" }
                Result.success(Unit)
            } else {
                val error = Exception("Failed to download file: ${response.status}")
                logger.error { error.message }
                Result.failure(error)
            }
        } catch (e: Exception) {
            logger.error(e) { "Error during file download" }
            Result.failure(e)
        }
    }

    /**
     * Download and install API package
     */
    private suspend fun downloadAndInstallApi(apiVersion: String): Result<Unit> {
        val apiUrl = "https://github.com/ArkServerApi/AsaApi/releases/download/$apiVersion/AsaApi_$apiVersion.zip"
        val zipFilePath = Path.of(profile.installationLocation, "AsaApi_$apiVersion.zip")

        val downloadResult = downloadFile(apiUrl, zipFilePath)
        if (downloadResult.isFailure) {
            return downloadResult
        }

        val extractResult = extractZip(zipFilePath.toFile(), profileBinaryPath)
        if (extractResult.isFailure) {
            return extractResult
        }

        try {
            if (zipFilePath.toFile().exists()) {
                zipFilePath.toFile().delete()
                logger.info { "Removed API zip file $zipFilePath" }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error cleaning up API zip file" }
        }

        return Result.success(Unit)
    }

    /**
     * Download and install version loader DLL
     */
    private suspend fun downloadAndInstallVersionDll(): Result<Unit> {
        val versionDllUrl = "https://github.com/ArkServerApi/AsaApiLoader/releases/latest/download/VersionLoader.zip"
        val versionZipPath = Path.of(profile.installationLocation, "VersionLoader.zip")

        val downloadResult = downloadFile(versionDllUrl, versionZipPath)
        if (downloadResult.isFailure) {
            return downloadResult
        }

        val extractResult = extractZip(versionZipPath.toFile(), profileBinaryPath)
        if (extractResult.isFailure) {
            return extractResult
        }

        try {
            if (versionZipPath.toFile().exists()) {
                versionZipPath.toFile().delete()
                logger.info { "Removed version zip file $versionZipPath" }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error cleaning up version DLL zip file" }
        }

        return Result.success(Unit)
    }

    /**
     * Update API version file
     */
    private fun updateApiVersionFile(apiVersion: String): Result<Unit> {
        return try {
            val apiVersionFile = Path.of(profile.installationLocation, "apiversion.txt")
            apiVersionFile.toFile().writeText(apiVersion)
            logger.info { "Updated API version file to $apiVersion" }
            Result.success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Error writing API version file" }
            Result.failure(e)
        }
    }

    suspend fun installApi(installVersionDll: Boolean = true): Result<Unit> {
        val latestRelease = getLatestApiRelease()
        if (latestRelease.isFailure) {
            return Result.failure(latestRelease.exceptionOrNull() ?: Exception("Failed to fetch latest API release"))
        }
        val apiVersion = latestRelease.getOrNull() ?: return Result.failure(Exception("API version is null"))
        logger.info { "Installing API version $apiVersion" }

        val installedApiPath = Path.of(profile.installationLocation, "apiversion.txt")
        if (installedApiPath.toFile().exists()) {
            val installedVersion = installedApiPath.toFile().readText().trim()
            if (installedVersion == apiVersion) {
                logger.info { "API is already installed at version $installedVersion" }
                return Result.success(Unit)
            }
        }

        val apiInstallResult = downloadAndInstallApi(apiVersion)
        if (apiInstallResult.isFailure) {
            return apiInstallResult
        }

        if (installVersionDll) {
            val versionDllResult = downloadAndInstallVersionDll()
            if (versionDllResult.isFailure) {
                return versionDllResult
            }
        }

        return updateApiVersionFile(apiVersion)
    }

    /**
     * Initialize SteamCMD installation
     */
    private fun initializeSteamCmd(): Result<String> {
        val steamcmdPathResult = SettingsHelper().getSettings()
        if (steamcmdPathResult.isFailure) {
            return Result.failure(steamcmdPathResult.exceptionOrNull() ?: Exception("Failed to get SteamCMD path"))
        }
        val steamcmdPath = steamcmdPathResult.getOrNull()?.steamCmdPath
            ?: return Result.failure(Exception("SteamCMD path is not configured"))

        val steamInstallResult = Installer(Path.of(steamcmdPath)).install()
        if (steamInstallResult.isFailure) {
            return Result.failure(
                steamInstallResult.exceptionOrNull() ?: Exception("Failed to install SteamCMD")
            )
        }

        return Result.success(steamcmdPath)
    }

    /**
     * Run SteamCMD to install/update the server
     */
    private suspend fun runSteamCmdInstall(
        steamcmdPath: String,
        onStatusUpdate: (status: Status) -> Unit,
        validate: Boolean
    ): Result<Unit> {
        val statusFlow = SteamCMD(Path.of(steamcmdPath)).runAsFlow(
            listOf(
                "force_install_dir ${profile.installationLocation}",
                "login anonymous",
                if (validate) {
                    "app_update $arkId validate"
                } else {
                    "app_update $arkId"
                },
                "quit"
            )
        )

        statusFlow.collect { status ->
            onStatusUpdate(status)
            logger.info { "Install/Update status update: $status" }
        }

        return Result.success(Unit)
    }

    suspend fun install(installApi: Boolean = true, onStatusUpdate: (status: Status) -> Unit): Result<Unit> {
        if (installerRunning()) {
            return Result.failure(Exception("Installation is already running, please wait until it completes."))
        }
        setInstalling(true)
        logger.info { "Starting installation for ${profile.profileName} (${profile.uuid})" }

        try {
            if (installApi) {
                val apiInstallResult = installApi()
                if (apiInstallResult.isFailure) {
                    return apiInstallResult
                }
            }

            val steamcmdPathResult = initializeSteamCmd()
            if (steamcmdPathResult.isFailure) {
                return Result.failure(
                    steamcmdPathResult.exceptionOrNull() ?: Exception("Failed to initialize SteamCMD")
                )
            }
            val steamcmdPath =
                steamcmdPathResult.getOrNull() ?: return Result.failure(Exception("SteamCMD path is null"))

            return runSteamCmdInstall(steamcmdPath, onStatusUpdate, true)
        } finally {
            setInstalling(false)
        }
    }

    companion object {
        private var isInstalling = AtomicBoolean(false)

        fun installerRunning(): Boolean {
            return isInstalling.load()
        }

        private fun setInstalling(value: Boolean) {
            isInstalling.exchange(value)
        }
    }
}