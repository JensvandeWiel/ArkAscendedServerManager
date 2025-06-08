@file:OptIn(ExperimentalUuidApi::class)

package server

import Installer
import Status
import SteamCMD
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.isSuccess
import io.ktor.utils.io.copyTo
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import settings.SettingsHelper
import java.nio.file.Path
import kotlin.text.get
import kotlin.uuid.ExperimentalUuidApi

class ServerManager(private val _profile: ServerProfile) {
    private val logger = KotlinLogging.logger("ServerManager (${_profile.uuid})")
    private val httpClient = HttpClient(CIO)
    fun isInstalled(): Boolean {
        // Check if the installation directory exists
        val installationPath =
            Path.of(_profile.installationLocation).resolve("ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe")
        return installationPath.toFile().exists()
    }


    private suspend fun getLatestApiRelease(): Result<String> {
        val serverPath = Path.of(_profile.installationLocation, "ShooterGame\\Binaries\\Win64")
        if (!serverPath.toFile().exists()) {
            serverPath.toFile().mkdirs()
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

    suspend fun installApi(installVersionDll: Boolean = true): Result<Unit> {
        val latestRelease = getLatestApiRelease()
        if (latestRelease.isFailure) {
            return Result.failure(latestRelease.exceptionOrNull() ?: Exception("Failed to fetch latest API release"))
        }
        val apiVersion = latestRelease.getOrNull() ?: return Result.failure(Exception("API version is null"))
        val apiUrl = "https://github.com/ArkServerApi/AsaApi/releases/download/$apiVersion/AsaApi_$apiVersion.zip"
        logger.info { "Installing API version $apiVersion from $apiUrl" }

        // Check installed version
        val installedApiPath = Path.of(_profile.installationLocation, "apiversion.txt")
        if (installedApiPath.toFile().exists()) {
            // Read the installed version
            val installedVersion = installedApiPath.toFile().readText().trim()
            if (installedVersion == apiVersion) {
                logger.info { "API is already installed at version $installedVersion" }
                return Result.success(Unit)
            }
        }


        // Get zip file URL from the API release
        try {
            val response = httpClient.get(apiUrl)
            if (response.status.isSuccess()) {
                val zipFilePath = Path.of(_profile.installationLocation, "AsaApi_$apiVersion.zip")
                response.bodyAsChannel().copyTo(zipFilePath.toFile().outputStream())
                logger.info { "API downloaded successfully at $zipFilePath" }
            } else {
                return Result.failure(Exception("Failed to download API: ${response.status}"))
            }
        } catch (e: Exception) {
            logger.error(e) { "Error during API downloading" }
            return Result.failure(e)
        }

        // Extract the downloaded zip file
        try {
            val zipFilePath = Path.of(_profile.installationLocation, "AsaApi_$apiVersion.zip")
            val extractPath = Path.of(_profile.installationLocation, "ShooterGame\\Binaries\\Win64")
            java.util.zip.ZipFile(zipFilePath.toFile()).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    if (!entry.isDirectory) {
                        val outputFile = extractPath.resolve(entry.name)
                        outputFile.parent.toFile().mkdirs()
                        zip.getInputStream(entry).use { input ->
                            outputFile.toFile().outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            logger.info { "API extracted successfully to $extractPath" }
        } catch (e: Exception) {
            logger.error(e) { "Error during API extraction" }
            return Result.failure(e)
        }

        // Clean up the zip file
        try {
            val zipFilePath = Path.of(_profile.installationLocation, "AsaApi_$apiVersion.zip")
            if (zipFilePath.toFile().exists()) {
                zipFilePath.toFile().delete()
                logger.info { "Removed zip file $zipFilePath" }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error during zip file cleanup" }
            return Result.failure(e)
        }

        if (installVersionDll) {
            val versionDllUrl = "https://github.com/ArkServerApi/AsaApiLoader/releases/latest/download/VersionLoader.zip"
            try {
                val versionResponse = httpClient.get(versionDllUrl)
                if (versionResponse.status.isSuccess()) {
                    val versionZipPath = Path.of(_profile.installationLocation, "VersionLoader.zip")
                    val versionExtractPath = Path.of(_profile.installationLocation, "ShooterGame\\Binaries\\Win64")
                    versionResponse.bodyAsChannel().copyTo(versionZipPath.toFile().outputStream())
                    logger.info { "Version DLL downloaded successfully at $versionZipPath" }

                    // Extract the version DLL
                    java.util.zip.ZipFile(versionZipPath.toFile()).use { zip ->
                        zip.entries().asSequence().forEach { entry ->
                            if (!entry.isDirectory) {
                                val outputFile = Path.of(versionExtractPath.toString(), entry.name)
                                outputFile.parent.toFile().mkdirs()
                                zip.getInputStream(entry).use { input ->
                                    outputFile.toFile().outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                            }
                        }
                    }
                    logger.info { "Version DLL extracted successfully to $versionExtractPath" }

                    // Clean up the version zip file
                    if (versionZipPath.toFile().exists()) {
                        versionZipPath.toFile().delete()
                        logger.info { "Removed version zip file $versionZipPath" }
                    }
                } else {
                    return Result.failure(Exception("Failed to download Version DLL: ${versionResponse.status}"))
                }
            } catch (e: Exception) {
                logger.error(e) { "Error during Version DLL downloading or extraction" }
                return Result.failure(e)
            }
        }

        try {
            val apiVersionFile = Path.of(_profile.installationLocation, "apiversion.txt")
            apiVersionFile.toFile().writeText(apiVersion)
            logger.info { "Updated API version file to $apiVersion" }
        } catch (e: Exception) {
            logger.error(e) { "Error writing API version file" }
            return Result.failure(e)
        }

        logger.info { "API installation completed successfully for ${_profile.profileName} (${_profile.uuid})" }
        return Result.success(Unit)
    }

    suspend fun install(installApi: Boolean = true, onStatusUpdate: (status: Status) -> Unit): Result<Unit> {
        if (installerRunning()) {
            return Result.failure(Exception("Installation is already running, please wait until it completes."))
        }
        setInstalling(true)
        logger.info { "Starting installation for ${_profile.profileName} (${_profile.uuid})" }
        // Install API if requested
        if (installApi) {
            val apiInstallResult = installApi()
            if (apiInstallResult.isFailure) {
                setInstalling(false)
                return Result.failure(apiInstallResult.exceptionOrNull() ?: Exception("Unknown error during API installation"))
            }
        }
        // Get steamcmd path from the global settings
        val steamcmdPath = SettingsHelper().getSettings().getOrElse {
            return Result.failure(it)
        }.steamCmdPath

        val steamInstallResult = Installer(Path.of(steamcmdPath)).install()
        if (steamInstallResult.isFailure) {
            return Result.failure(
                steamInstallResult.exceptionOrNull() ?: Exception("Unknown error during installation")
            )
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
        setInstalling(false)
        return Result.success(Unit)
    }

    companion object {
        private var installing = false
        fun installerRunning(): Boolean {
            return installing
        }

        private fun setInstalling(value: Boolean) {
            installing = value
        }
    }
}