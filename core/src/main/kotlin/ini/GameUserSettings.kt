package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniBoolean
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.OmitIfNull
import annotations.IniString
import annotations.WithIgnored
import eu.wynq.arkascendedservermanager.core.server.DefaultInt
import eu.wynq.arkascendedservermanager.core.server.DefaultString
import eu.wynq.arkascendedservermanager.core.support.isValidUrl
import io.ktor.http.Url
import kotlinx.serialization.Serializable
import support.Unsure

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
    @IniProperty("MaxTributeDinos")
    val maxTributeDinos: Int = 20,
    @IniProperty("MaxTributeItems")
    val maxTributeItems: Int = 50,
    @IniProperty("MinimumDinoReuploadInterval")
    @OmitIfNull
    @field:DefaultInt(0)
    val minimumDinoReuploadInterval: Int? = null,
    @IniProperty("TributeCharacterExpirationSeconds")
    @OmitIfNull
    @field:DefaultInt(0)
    val tributeCharacterExpirationSeconds: Int? = null,
    @IniProperty("TributeDinoExpirationSeconds")
    @OmitIfNull
    @field:DefaultInt(86400)
    val tributeDinoExpirationSeconds: Int? = null,
    @IniProperty("TributeItemExpirationSeconds")
    @OmitIfNull
    @field:DefaultInt(86400)
    val tributeItemExpirationSeconds: Int? = null,
    @IniProperty("BanListURL")
    @OmitIfNull
    @IniString(true)
    @field:DefaultString("https://cdn2.arkdedicated.com/asa/BanList.txt")
    val banListUrl: String? = null,
    @IniProperty("CustomDynamicConfigUrl")
    @OmitIfNull
    @Unsure
    @IniString(true)
    val customDynamicConfigUrl: String? = null,
    @IniProperty("CustomLiveTuningUrl")
    @OmitIfNull
    @IniString(true)
    @field:DefaultString("https://cdn2.arkdedicated.com/asa/livetuningoverloads.json")
    val customLiveTuningUrl: String? = null,
    @IniProperty("TamingSpeedMultiplier")
    val tamingSpeedMultiplier: Float = 1.0f,
    @IniProperty("HarvestAmountMultiplier")
    val harvestAmountMultiplier: Float = 1.0f,
    @IniProperty("ResourcesRespawnPeriodMultiplier")
    val resourcesRespawnPeriodMultiplier: Float = 1.0f,
    @IniProperty("ClampResourceHarvestDamage")
    val clampResourceHarvestDamage: Boolean = false,
    @IniProperty("HarvestHealthMultiplier")
    val harvestHealthMultiplier: Float = 1.0f,
    @IniProperty("DayCycleSpeedScale")
    val dayCycleSpeedScale: Float = 1.0f,
    @IniProperty("DayTimeSpeedScale")
    val dayTimeSpeedScale: Float = 1.0f,
    @IniProperty("NightTimeSpeedScale")
    val nightTimeSpeedScale: Float = 1.0f,
    @IniProperty("DisableWeatherFog")
    val disableWeatherFog: Boolean = false,
    @IniProperty("ClampItemSpoilingTimes")
    val clampItemSpoilingTimes: Boolean = false,
    @IniProperty("ServerHardcore")
    val serverHardcore: Boolean = false,
    @IniProperty("EnableExtraStructurePreventionVolumes")
    val enableExtraStructurePreventionVolumes: Boolean = false,
    @IniProperty("serverPVE")
    val serverPVE: Boolean = false,
    @IniProperty("AllowCaveBuildingPvE")
    val allowCaveBuildingPvE: Boolean = false,
    @IniProperty("AllowCaveBuildingPvP")
    val allowCaveBuildingPvP: Boolean = false,
    @IniProperty("AllowCrateSpawnsOnTopOfStructures")
    val allowCrateSpawnsOnTopOfStructures: Boolean = false,
    @IniProperty("EnableCryoSicknessPVE")
    val enableCryoSicknessPVE: Boolean = false,
    @IniProperty("RandomSupplyCratePoints")
    val randomSupplyCratePoints: Boolean = false,
    @IniProperty("TribeNameChangeCooldown")
    val tribeNameChangeCooldown: Int = 15 * 60,
    @IniProperty("PreventTribeAlliances")
    val preventTribeAlliances: Boolean = false,
    @IniProperty("PreventDiseases")
    val preventDiseases: Boolean = false,
    @IniProperty("NonPermanentDiseases")
    val nonPermanentDiseases: Boolean = false,
    @IniProperty("AllowFlyerCarryPvE")
    val allowFlyerCarryPvE: Boolean = false,
    @IniProperty("XPMultiplier")
    val xpMultiplier: Float = 1.0f,
    @IniProperty("PlayerDamageMultiplier")
    val playerDamageMultiplier: Float = 1.0f,
    @IniProperty("PlayerResistanceMultiplier")
    val playerResistanceMultiplier: Float = 1.0f,
    @IniProperty("PlayerCharacterWaterDrainMultiplier")
    val playerCharacterWaterDrainMultiplier: Float = 1.0f,
    @IniProperty("PlayerCharacterFoodDrainMultiplier")
    val playerCharacterFoodDrainMultiplier: Float = 1.0f,
    @IniProperty("PlayerCharacterStaminaDrainMultiplier")
    val playerCharacterStaminaDrainMultiplier: Float = 1.0f,
    @IniProperty("PlayerCharacterHealthRecoveryMultiplier")
    val playerCharacterHealthRecoveryMultiplier: Float = 1.0f,
    @IniProperty("AllowRaidDinoFeeding")
    val allowRaidDinoFeeding: Boolean = false,
    @IniProperty("DinoCharacterHealthRecoveryMultiplier")
    val dinoCharacterHealthRecoveryMultiplier: Float = 1.0f,
    @IniProperty("DinoCharacterStaminaDrainMultiplier")
    val dinoCharacterStaminaDrainMultiplier: Float = 1.0f,
    @IniProperty("DinoDamageMultiplier")
    val dinoDamageMultiplier: Float = 1.0f,
    @IniProperty("DinoResistanceMultiplier")
    val dinoResistanceMultiplier: Float = 1.0f,
    @IniProperty("MaxPersonalTamedDinos")
    val maxPersonalTamedDinos: Int = 0,
    @IniProperty("MaxTamedDinos")
    val maxTamedDinos: Int = 5000,
    @IniProperty("RaidDinoCharacterFoodDrainMultiplier")
    val raidDinoCharacterFoodDrainMultiplier: Float = 1.0f,
    @IniProperty("TamedDinoDamageMultiplier")
    @Unsure
    val tamedDinoDamageMultiplier: Float = 1.0f,
    @IniProperty("TamedDinoResistanceMultiplier")
    @Unsure
    val tamedDinoResistanceMultiplier: Float = 1.0f,
) {
    fun validateAutoSavePeriodMinutes() = autoSavePeriodMinutes >= 0
    fun validateKickIdlePlayersPeriod() = kickIdlePlayersPeriod >= 0
    fun validateRconServerGameLogBuffer() = rconServerGameLogBuffer >= 0
    fun validateMaxTributeDinos() = maxTributeDinos >= 20 && maxTributeDinos <= 100
    fun validateMaxTributeItems() = maxTributeItems >= 50 && maxTributeItems <= 100
    fun validateBanListUrl() = banListUrl == null || (banListUrl.isNotBlank() && banListUrl.isValidUrl())
    fun validateCustomDynamicConfigUrl() = customDynamicConfigUrl == null || (customDynamicConfigUrl.isNotBlank() && customDynamicConfigUrl.isValidUrl())
    fun validateTribeNameChangeCooldown() = tribeNameChangeCooldown >= 0
    fun validate() = validateAutoSavePeriodMinutes() && validateKickIdlePlayersPeriod() && validateRconServerGameLogBuffer() && validateMaxTributeDinos() && validateMaxTributeItems() && validateBanListUrl() && validateCustomDynamicConfigUrl() && validateTribeNameChangeCooldown()
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