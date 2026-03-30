package eu.wynq.arkascendedservermanager.ui.features.server

import eu.wynq.arkascendedservermanager.core.db.models.Server

data class ServerModel(
    val server: Server? = null,
    val initialServer: Server? = null
) {
    fun isDirty() = server != initialServer
}

