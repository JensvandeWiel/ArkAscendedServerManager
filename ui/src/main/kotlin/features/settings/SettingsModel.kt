package eu.wynq.arkascendedservermanager.ui.features.settings

import eu.wynq.arkascendedservermanager.ui.helpers.isValidPath

data class SettingsModel(
    val dataPath: String = "",
    val dataPathError: Boolean = false,
    val initialDataPath: String = ""
) {
    fun isDirty() = dataPath != initialDataPath
    fun isDataPathValid() = isValidPath(dataPath)

    fun isValid() = isDataPathValid()
}
