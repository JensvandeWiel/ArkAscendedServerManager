package eu.wynq.arkascendedservermanager.ui.features.server

import eu.wynq.arkascendedservermanager.core.db.models.Server

data class ServerModel(
    val server: Server? = null,
    val initialServer: Server? = null,
    val isInstalled: Boolean? = null,
    val apiIsInstalled: Boolean? = null,
    val version: String? = null,
    val apiVersion: String? = null,
    val deleteDialogOpen: Boolean = false,
) {
    fun isDirty() = server != initialServer
    fun isValid() = server != null && server.validate()
    fun canSave() = isValid() && isDirty()
}

