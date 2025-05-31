package server

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class ServerConfig @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    val profileName: String,
)
