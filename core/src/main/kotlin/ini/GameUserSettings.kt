package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniBoolean
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

@IniSection("MessageOfTheDay")
@Serializable
data class MessageOfTheDay(
    @IniProperty("Duration")
    val duration: Int = 20,
    @IniProperty("Message")
    val message: String = ""
) {
    fun validate() = duration >= 0 // Message is optional
}

@IniSection("ServerSettings")
@Serializable
data class ServerSettings(
    @IniProperty("AutoSavePeriodMinutes")
    val autoSavePeriodMinutes: Int = 15,
    @IniProperty("KickIdlePlayersPeriod")
    val kickIdlePlayersPeriod: Int = 3600,
    @IniProperty("RCONServerGameLogBuffer")
    val rconServerGameLogBuffer: Int = 600,
    @IniProperty("CrossARKAllowForeignDinoDownloads")
    val crossArkAllowForeignDinoDownloads: Boolean = false,
    /*@IniProperty("MinimumDinoReuploadInterval")
    val minimumDinoReuploadInterval: Int = 0,*/
    @IniProperty("NoTributeDownloads")
    val noTributeDownloads: Boolean = true,
    @IniProperty("PreventDownloadDinos")
    val preventDownloadDinos: Boolean = true,
    @IniProperty("PreventDownloadItems")
    val preventDownloadItems: Boolean = true,
    @IniProperty("PreventDownloadSurvivors")
    val preventDownloadSurvivors: Boolean = true,
    @IniProperty("PreventUploadDinos")
    val preventUploadDinos: Boolean = true,
    @IniProperty("PreventUploadItems")
    val preventUploadItems: Boolean = true,
    @IniProperty("PreventUploadSurvivors")
    val preventUploadSurvivors: Boolean = true,
) {
    fun validateAutoSavePeriodMinutes() = autoSavePeriodMinutes >= 0
    fun validateKickIdlePlayersPeriod() = kickIdlePlayersPeriod >= 0
    fun validateRconServerGameLogBuffer() = rconServerGameLogBuffer >= 0
    fun validate() = validateAutoSavePeriodMinutes() && validateKickIdlePlayersPeriod() && validateRconServerGameLogBuffer()
}

@IniSerializable
@Serializable
data class GameUserSettings(
    override val ignoredKeys: IniFile = IniFile(),
    @IniSection
    val sessionSettings: SessionSettings = SessionSettings(),
    @IniSection
    val messageOfTheDay: MessageOfTheDay = MessageOfTheDay(),
    @IniSection
    val serverSettings: ServerSettings = ServerSettings(),

    ) : WithIgnored {
    companion object {
        fun createForNewServer(name: String) = GameUserSettings(
            sessionSettings = SessionSettings("$name Server hosted by JensvandeWiel/ArkAscendedServerManager"),
        )
    }

    fun validate() = sessionSettings.validate() && messageOfTheDay.validate() && serverSettings.validate()
}