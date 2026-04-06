package eu.wynq.arkascendedservermanager.core.support

import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    val tag_name: String,
    val assets: List<GithubAsset>
)

@Serializable
data class GithubAsset(
    val name: String,
    val browser_download_url: String,
    val content_type: String
)