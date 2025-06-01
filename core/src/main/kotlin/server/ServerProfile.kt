package server

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class ServerProfile @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    val profileName: String,
    val installationLocation: String,
) {
    fun getServerManager(): ServerManager {
        return ServerManager(this)
    }
}
