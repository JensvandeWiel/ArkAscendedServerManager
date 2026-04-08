package eu.wynq.arkascendedservermanager.core.server

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val serverName: String = "Server hosted by JensvandeWiel/ArkAscendedServerManager",
) {
    fun validateServerName() = serverName.isNotBlank()
    fun validate() = validateServerName()

    companion object {
        fun createForNewServer(name: String): Settings {
            return Settings(
                serverName = "$name Server hosted by JensvandeWiel/ArkAscendedServerManager"
            )
        }
    }
}
