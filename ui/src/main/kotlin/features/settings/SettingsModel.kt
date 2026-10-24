package eu.wynq.arkascendedservermanager.ui.features.settings

import eu.wynq.arkascendedservermanager.core.support.isValidPath
import eu.wynq.arkascendedservermanager.ui.helpers.UpdateChannel

data class SettingsModel(
    val dataPath: String = "",
    val dataPathError: Boolean = false,
    val steamCmdPath: String = "",
    val steamCmdPathError: Boolean = false,
    val updateChannel: UpdateChannel = UpdateChannel.STABLE,
    val initialUpdateChannel: UpdateChannel = UpdateChannel.STABLE,
    val initialSteamCmdPath: String = "",
    val initialDataPath: String = ""
) {
    fun isDirty() = dataPath != initialDataPath || steamCmdPath != initialSteamCmdPath || updateChannel != initialUpdateChannel
    fun isDataPathValid() = isValidPath(dataPath)

    fun isSteamCmdPathValid() = isValidPath(steamCmdPath)

    fun isValid() = isDataPathValid()
}
