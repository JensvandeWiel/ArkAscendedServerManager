package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.WithIgnored
import kotlinx.serialization.Serializable

@IniSection("SessionSettings")
@Serializable
data class SessionSettings(
    @IniProperty("SessionName")
    val sessionName: String = "Server hosted by JensvandeWiel/ArkAscendedServerManager",
) {
    fun validate() = validateSessionName()
    fun validateSessionName() = sessionName.isNotBlank()
}

@IniSerializable
@Serializable
data class GameUserSettings(
    override val ignoredKeys: IniFile = IniFile(),
    @IniSection
    val sessionSettings: SessionSettings = SessionSettings(),

    ) : WithIgnored {
    companion object {
        fun createForNewServer(name: String) = GameUserSettings(
            sessionSettings = SessionSettings("$name Server hosted by JensvandeWiel/ArkAscendedServerManager"),
        )
    }

    fun validate() = sessionSettings.validate()
}