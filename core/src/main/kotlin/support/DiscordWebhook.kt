package support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebhookPayload(
    val content: String? = null,
    val username: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val embeds: List<DiscordEmbed>? = null,
    val attachments: List<String> = emptyList()
)

@Serializable
data class DiscordEmbed(
    val title: String? = null,
    val color: Int? = null,
    val timestamp: String? = null
)