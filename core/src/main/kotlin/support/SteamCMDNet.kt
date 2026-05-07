package support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamResponse(
    val data: Map<String, AppData>,
    val status: String
)
@Serializable
data class AppData(
    val depots: Depots
)
@Serializable
data class Depots(
    val branches: Branches
)
@Serializable
data class Branches(
    val public: BranchInfo
)
@Serializable
data class BranchInfo(
    @SerialName("buildid")
    val buildId: String,
    @SerialName("timebuildupdated")
    val timeBuildUpdated: String,
    @SerialName("timeupdated")
    val timeUpdated: String
)
