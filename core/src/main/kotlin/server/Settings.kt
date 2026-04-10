package eu.wynq.arkascendedservermanager.core.server

import kotlinx.serialization.Serializable
import java.security.SecureRandom

@Serializable
data class Settings(
    val administration: Administration = Administration()
) {
    fun validate() = administration.validate()

    companion object {
        fun createForNewServer(): Settings {
            return Settings(
                Administration()
            )
        }
    }
}

@Serializable
data class Administration(
    val serverPassword: String? = null,
    val adminPassword: String = getRandomString(16),
    val serverPort: Int = 7777,
    val peerPort: Int = 7778,
    val queryPort: Int = 27015,
    val rconPort: Int = 27016,
    val rconEnabled: Boolean = true,
    val map: String = "TheIsland_WP",
    val slots: Int = 70,
    val mods: List<String> = emptyList()
) {
    fun validate() = validateServerPassword()
            && validateAdminPassword()
            && validateSlots()
            && validateMods()
            && validateMap()
            && validateQueryPort()
            && validatePeerPort()
            && validateRconPort()
            && validateServerPort()

    fun validateServerPassword() = serverPassword == null || serverPassword.isNotBlank()
    fun validateAdminPassword() = adminPassword.isNotBlank()
    fun validateSlots() = slots > 0
    fun validateMods() = mods.all { mod ->
        mod.isNotBlank() && mod.isNumeric()
    }

    fun validateMap() = map.isNotBlank()
    fun validateQueryPort() = queryPort in 1..65535
    fun validatePeerPort() = peerPort in 1..65535 && peerPort == serverPort + 1
    fun validateRconPort() = rconPort in 1..65535
    fun validateServerPort() = serverPort in 1..65535

}

private fun getRandomString(length: Int): String {
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    return (1..length)
        .map { allowedChars[random.nextInt(allowedChars.length)] }
        .joinToString("")
}

private fun String.isNumeric() = all { it.isDigit() }