package ui.server

import Status

data class InstallationModel(
    val isInstalling: Boolean = false,
    val progress: Float? = null,
    val status: Status,
    val message: String? = null,
)
