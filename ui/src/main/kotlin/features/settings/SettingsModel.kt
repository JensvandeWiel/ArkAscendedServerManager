package eu.wynq.arkascendedservermanager.ui.features.settings

import eu.wynq.arkascendedservermanager.core.support.isValidPath

data class SettingsModel(
    val dataPath: String = "",
    val dataPathError: Boolean = false,
    val steamCmdPath: String = "",
    val steamCmdPathError: Boolean = false,
    val initialSteamCmdPath: String = "",
    val initialDataPath: String = ""
) {
    fun isDirty() = dataPath != initialDataPath
    fun isDataPathValid() = isValidPath(dataPath)

    fun isSteamCmdPathValid() = isValidPath(steamCmdPath)

    fun isValid() = isDataPathValid()
}
