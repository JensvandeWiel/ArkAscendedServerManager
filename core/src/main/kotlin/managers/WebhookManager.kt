package managers

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.managers.PowerManager
import eu.wynq.arkascendedservermanager.core.managers.PowerState
import eu.wynq.arkascendedservermanager.core.support.isValidUrl
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import support.DiscordEmbed
import support.WebhookPayload

object WebhookManager {
    private val _client = HttpClient(CIO) {
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

    private suspend fun sendWebhook(url: String, content: WebhookPayload): Result<Unit> {
        if (url.isBlank()) return Result.failure(IllegalArgumentException("Webhook URL cannot be blank"))
        if (!url.isValidUrl()) return Result.failure(IllegalArgumentException("Invalid webhook URL"))

        return runCatching {
            val response = _client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(content)
            }

            if (response.status != HttpStatusCode.NoContent && response.status != HttpStatusCode.OK) {
                throw IllegalStateException("Failed to send webhook: ${response.status}: ${response.bodyAsText()}")
            }
        }
    }

    suspend fun sendPowerStateChange(server: Server, oldState: PowerState, newState: PowerState): Result<Unit> {
        if (oldState == newState || oldState == PowerState.Unknown) return Result.success(Unit)
        if (!server.settings.administration.sendServerUpdatesEvents.contains(newState)) return Result.success(Unit)
        if (server.settings.administration.sendServerUpdatesWebhookUrl.isNullOrBlank()) return Result.success(Unit)

        return sendWebhook(
            server.settings.administration.sendServerUpdatesWebhookUrl, WebhookPayload(
                content = null,
                username = "Server Updates",
                avatarUrl = "https://repository-images.githubusercontent.com/706114586/b4dcc535-e66f-4a56-b634-6c68223ebe9e",
                attachments = emptyList(),
                embeds = listOf(
                    DiscordEmbed(
                        title = "Server ${server.profileName} changed to $newState",
                        color = PowerManager.getPowerStateColor(newState).toInt() and 0xFFFFFF,
                        timestamp = java.time.Instant.now().toString()
                    )
                )
            )
        )
    }
}