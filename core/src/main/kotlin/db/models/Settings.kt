package eu.wynq.arkascendedservermanager.core.db.models

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object SettingsTable : IntIdTable("settings") {
    val data_path = varchar("data_path", 255).default("C:\\\\aasm")
    val steamcmd_path = varchar("steamcmd_path", 255).default("C:\\\\steamcmd")
}

class SettingsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SettingsEntity>(SettingsTable)
    var data_path by SettingsTable.data_path
    var steamcmd_path by SettingsTable.steamcmd_path
}

data class Settings(val dataPath: String = "C:\\aasm", val steamcmdPath: String = "C:\\steamcmd") {
    companion object {
        fun fromEntity(entity: SettingsEntity) = Settings(
            dataPath = entity.data_path,
            steamcmdPath = entity.steamcmd_path
        )
    }

}