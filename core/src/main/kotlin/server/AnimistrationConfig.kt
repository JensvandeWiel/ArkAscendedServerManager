package server

import java.security.SecureRandom
import kotlinx.serialization.Serializable

@Serializable
data class AdministrationConfig(
    val serverName: String,
    val serverPassword: String? = null,
    val adminPassword: String = getRandomString(16),
    val serverPort: Int = 7777,
    val queryPort: Int = 27015,
    val rconPort: Int = 27016,
    val rconEnabled: Boolean = true,
    val rconPassword: String = getRandomString(16),
    val map: String = "TheIsland_WP",
)


private fun getRandomString(length: Int): String {
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    return (1..length)
        .map { allowedChars[random.nextInt(allowedChars.length)] }
        .joinToString("")
}