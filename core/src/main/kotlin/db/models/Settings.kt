package eu.wynq.arkascendedservermanager.core.db.models

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object SettingsTable : IntIdTable("settings") {
    val data_path = varchar("data_path", 255).default("C:\\\\aasm")
}

class SettingsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SettingsEntity>(SettingsTable)
    var data_path by SettingsTable.data_path
}

data class Settings(val dataPath: String = "C:\\aasm") {
    companion object {
        fun fromEntity(entity: SettingsEntity) = Settings(
            dataPath = entity.data_path
        )
    }

}