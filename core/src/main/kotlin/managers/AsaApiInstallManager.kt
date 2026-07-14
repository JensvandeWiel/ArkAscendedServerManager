package eu.wynq.arkascendedservermanager.core.managers

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.support.Constants
import eu.wynq.arkascendedservermanager.core.support.GithubRelease
import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.json.Json
import support.getExeVersion
import java.util.zip.ZipInputStream
import kotlin.io.path.Path
import kotlin.io.path.isSymbolicLink


data class AsaApiRelease(
    val version: Version,
    val downloadUrl: String,
) : Comparable<AsaApiRelease> {
    override fun compareTo(other: AsaApiRelease): Int = version.compareTo(other.version)
}


object AsaApiInstallManager {
    private val asaApiUrl = "https://api.github.com/repos/ArkServerApi/AsaApi/releases"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        headers {
            append("User-Agent", "JensvandeWiel/ArkAscendedServerManager")
        }
    }

    private fun parseAsaApiVersion(rawVersion: String): Version {
        return normalizeAsaApiVersion(rawVersion).toVersion(false)
    }

    private fun normalizeAsaApiVersion(rawVersion: String): String {
        val trimmed = rawVersion.trim().trimStart('v', 'V')
        val buildSeparator = trimmed.indexOf('+')
        val coreAndPrerelease = if (buildSeparator >= 0) trimmed.substring(0, buildSeparator) else trimmed
        val buildMetadata = if (buildSeparator >= 0) trimmed.substring(buildSeparator + 1) else null

        val prereleaseSeparator = coreAndPrerelease.indexOf('-')
        val core = if (prereleaseSeparator >= 0) coreAndPrerelease.substring(0, prereleaseSeparator) else coreAndPrerelease
        val prerelease = if (prereleaseSeparator >= 0) coreAndPrerelease.substring(prereleaseSeparator + 1) else null

        val normalizedCore = core
            .split('.')
            .filter { it.isNotBlank() }
            .map { segment -> segment.toIntOrNull()?.toString() ?: segment }
            .let { segments ->
                when (segments.size) {
                    1 -> "${segments[0]}.0.0"
                    2 -> "${segments[0]}.${segments[1]}.0"
                    else -> segments.take(3).joinToString(".")
                }
            }

        return buildString {
            append(normalizedCore)
            if (!prerelease.isNullOrBlank()) {
                append('-')
                append(prerelease)
            }
            if (!buildMetadata.isNullOrBlank()) {
                append('+')
                append(buildMetadata)
            }
        }
    }

    suspend fun getLatestRelease(): Result<AsaApiRelease> = runCatching {
        val request = client.get(asaApiUrl)
        if (request.status != HttpStatusCode.OK) {
            throw IllegalStateException("Failed to fetch releases: ${request.status}")
        }

        val releases: Array<GithubRelease> = request.body()

        val latest = releases.maxByOrNull { parseAsaApiVersion(it.tag_name) }
            ?: throw IllegalStateException("No releases found")

        val apiAsset = latest.assets.firstOrNull {
            it.content_type == "application/x-zip-compressed" && it.name == "AsaApi_${latest.tag_name.trim('v')}.zip"
        } ?: throw IllegalStateException("No suitable asset found for latest release")

        return Result.success(
            AsaApiRelease(
                version = parseAsaApiVersion(latest.tag_name),
                downloadUrl = apiAsset.browser_download_url
            )
        )
    }

    suspend fun install(server: Server, release: AsaApiRelease): Result<Unit> = runCatching {
        val installPath = Path(server.installationLocation, Constants.SERVER_BINARY_PATH)
        client.prepareGet(release.downloadUrl).execute { httpResponse ->
            if (httpResponse.status.value !in 200..299) {
                error("Failed to download: ${httpResponse.status}")
            }
            val channel = httpResponse.bodyAsChannel()

            channel.toInputStream().use { inputStream ->
                ZipInputStream(inputStream).use { zipStream ->
                    var entry = zipStream.nextEntry

                    while (entry != null) {
                        val targetPath = installPath.resolve(entry.name).normalize()
                        if (!targetPath.startsWith(installPath)) {
                            throw SecurityException("Zip entry is outside of the installation directory: ${entry.name}")
                        }
                        if (targetPath.isSymbolicLink()) {
                            throw SecurityException("Zip entry is a symbolic link, which is not allowed: ${entry.name}")
                        }

                        val newFile = targetPath.toFile()

                        if (entry.isDirectory) {
                            newFile.mkdirs()
                        } else {
                            newFile.parentFile?.mkdirs()

                            newFile.outputStream().use { fos ->
                                zipStream.copyTo(fos)
                            }
                        }
                        zipStream.closeEntry()
                        entry = zipStream.nextEntry
                    }
                }
            }
        }

        val versionFile = Path(server.installationLocation, Constants.ASA_API_VERSION_PATH).toFile()
        versionFile.writeText(release.version.toString())
    }

    fun getApiVersion(server: Server): Version? {
        val versionFile = Path(server.installationLocation, Constants.ASA_API_VERSION_PATH).toFile()
        if (versionFile.exists()) {
            return runCatching { parseAsaApiVersion(versionFile.readText()) }.getOrNull()
        }
        return null
    }

    fun getApiVersionAsString(server: Server): String? {
        val version = getApiVersion(server) ?: return null
        return version.toString()
    }

    fun isInstalled(server: Server): Boolean {
        val versionFile = Path(server.installationLocation, Constants.ASA_API_VERSION_PATH).toFile()
        return versionFile.exists()
    }

    fun isOverseerInstalled(server: Server): Boolean {
        val overseerFile =
            Path(server.installationLocation, Constants.SERVER_BINARY_PATH, Constants.OVERSEER_EXECUTABLE_NAME).toFile()
        return overseerFile.exists()
    }

    fun getOverseerVersion(server: Server): Version? {
        val overseerFile =
            Path(server.installationLocation, Constants.SERVER_BINARY_PATH, Constants.OVERSEER_EXECUTABLE_NAME).toFile()
        if (!overseerFile.exists()) return null

        return getExeVersion(overseerFile.toPath().toString())
    }

    fun getOverseerVersionAsString(server: Server): String? {
        val version = getOverseerVersion(server) ?: return null
        return "${version.major}.${version.minor}.${version.patch}"
    }
}