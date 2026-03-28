package eu.wynq.arkascendedservermanager.core.db.tables

import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object Servers : UuidTable("servers") {
    val name = varchar("name", 255)
}