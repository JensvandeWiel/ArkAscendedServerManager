package server

import IniFile
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.WithIgnored
import kotlinx.serialization.Serializable

@IniSection("ServerSettings")
@Serializable
data class ServerSettings(
    @IniProperty("AutoSavePeriodMinutes")
    val autoSavePeriodMinutes: Float = 15f
)

@IniSerializable
@Serializable
data class GameUserSettings(
    override val ignoredKeys: IniFile = IniFile(),
    val serverSettings: ServerSettings = ServerSettings(),
) : WithIgnored
