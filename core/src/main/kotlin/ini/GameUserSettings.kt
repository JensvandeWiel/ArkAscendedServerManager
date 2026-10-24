package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniArray
import annotations.IniBoolean
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.OmitIfNull
import annotations.IniString
import annotations.WithIgnored
import ArrayType
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
    @IniProperty("MultiHome")
    @Unsure
    @OmitIfNull
    val multiHome: String? = null,
    ) {
    fun validate() = validateSessionName() && validateMultiHome()
    fun validateSessionName() = sessionName.isNotBlank()
    fun validateMultiHome() = multiHome == null || multiHome.isNotBlank()
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

    @IniProperty("ActiveMods")
    @IniArray(arrayType = ArrayType.CommaSeparatedArray)
    val activeMods: List<Int> = emptyList(),

    @IniProperty("ActiveMapMod")
    @OmitIfNull
    val activeMapMod: Int? = null,

    @IniProperty("DifficultyOffset")
    val difficultyOffset: Float = 1.0f,

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
    @Unsure
    @OmitIfNull
    @field:DefaultInt(0)
    val minimumDinoReuploadInterval: Int? = null,
    @IniProperty("TributeCharacterExpirationSeconds")
    @Unsure
    @OmitIfNull
    @field:DefaultInt(0)
    val tributeCharacterExpirationSeconds: Int? = null,
    @IniProperty("TributeDinoExpirationSeconds")
    @Unsure
    @OmitIfNull
    @field:DefaultInt(86400)
    val tributeDinoExpirationSeconds: Int? = null,
    @IniProperty("TributeItemExpirationSeconds")
    @Unsure
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
    val allowCaveBuildingPvP: Boolean = true,
    @IniProperty("AllowCrateSpawnsOnTopOfStructures")
    @Unsure
    val allowCrateSpawnsOnTopOfStructures: Boolean = false,
    @IniProperty("EnableCryoSicknessPVE")
    @Unsure
    val enableCryoSicknessPVE: Boolean = false,
    @IniProperty("RandomSupplyCratePoints")
    val randomSupplyCratePoints: Boolean = false,
    @IniProperty("StructureResistanceMultiplier")
    val structureResistanceMultiplier: Float = 1.0f,
    @IniProperty("StructurePreventResourceRadiusMultiplier")
    val structurePreventResourceRadiusMultiplier: Float = 1.0f,
    @IniProperty("TheMaxStructuresInRange")
    val theMaxStructuresInRange: Int = 10500,
    @IniProperty("IgnoreLimitMaxStructuresInRangeTypeFlag")
    val ignoreLimitMaxStructuresInRangeTypeFlag: Boolean = false,
    @IniProperty("PerPlatformMaxStructuresMultiplier")
    val perPlatformMaxStructuresMultiplier: Float = 1.0f,
    @IniProperty("PlatformSaddleBuildAreaBoundsMultiplier")
    val platformSaddleBuildAreaBoundsMultiplier: Float = 1.0f,
    @IniProperty("OverrideStructurePlatformPrevention")
    val overrideStructurePlatformPrevention: Boolean = false,
    @IniProperty("AlwaysAllowStructurePickup")
    val alwaysAllowStructurePickup: Boolean = false,
    @IniProperty("StructurePickupTimeAfterPlacement")
    val structurePickupTimeAfterPlacement: Float = 30.0f,
    @IniProperty("StructurePickupHoldDuration")
    val structurePickupHoldDuration: Float = 0.5f,
    @IniProperty("DisableStructureDecayPvE")
    val disableStructureDecayPvE: Boolean = false,
    @IniProperty("PvEStructureDecayPeriodMultiplier")
    @Unsure
    val pvEStructureDecayPeriodMultiplier: Float = 1.0f,
    @IniProperty("PvPStructureDecay")
    @Unsure
    val pvPStructureDecay: Boolean = false,
    @IniProperty("AutoDestroyOldStructuresMultiplier")
    @Unsure
    val autoDestroyOldStructuresMultiplier: Float = 0.0f,
    @IniProperty("ItemStackSizeMultiplier")
    val itemStackSizeMultiplier: Float = 1.0f,
    @IniProperty("ClampItemStats")
    @Unsure
    val clampItemStats: Boolean = false,
    @IniProperty("MaxPlatformSaddleStructureLimit")
    @Unsure
    val maxPlatformSaddleStructureLimit: Int = 75,
    @IniProperty("AdminLogging")
    val adminLogging: Boolean = false,
    @IniProperty("globalVoiceChat")
    val globalVoiceChat: Boolean = false,
    @IniProperty("ProximityChat")
    val proximityChat: Boolean = false,
    @IniProperty("ServerCrosshair")
    val serverCrosshair: Boolean = true,
    @IniProperty("ShowMapPlayerLocation")
    val showMapPlayerLocation: Boolean = true,
    @IniProperty("AllowThirdPersonPlayer")
    val allowThirdPersonPlayer: Boolean = true,
    @IniProperty("AllowHitMarkers")
    val allowHitMarkers: Boolean = true,
    @IniProperty("AllowHideDamageSourceFromLogs")
    val allowHideDamageSourceFromLogs: Boolean = true,
    @IniProperty("ShowFloatingDamageText")
    val showFloatingDamageText: Boolean = false,
    @IniProperty("ServerForceNoHUD")
    val serverForceNoHud: Boolean = false,
    @IniProperty("DisablePvEGamma")
    val disablePvEGamma: Boolean = false,
    @IniProperty("EnablePvPGamma")
    val enablePvPGamma: Boolean = false,
    @IniProperty("PreventSpawnAnimations")
    val preventSpawnAnimations: Boolean = false,
    @IniProperty("DontAlwaysNotifyPlayerJoined")
    val dontAlwaysNotifyPlayerJoined: Boolean = false,
    @IniProperty("BadWordListURL")
    @OmitIfNull
    @IniString(true)
    val badWordListUrl: String? = null,
    @IniProperty("BadWordWhiteListURL")
    @OmitIfNull
    @IniString(true)
    val badWordWhiteListUrl: String? = null,
    @IniProperty("AdminListURL")
    @OmitIfNull
    @IniString(true)
    val adminListUrl: String? = null,
    @IniProperty("CosmeticWhitelistOverride")
    @OmitIfNull
    @IniString(true)
    val cosmeticWhitelistOverride: String? = null,
    @IniProperty("UseCharacterTracker")
    val useCharacterTracker: Boolean = false,
    @IniProperty("UpdateAllowedCheatersInterval")
    val updateAllowedCheatersInterval: Float = 600.0f,
    @IniProperty("ServerEnableMeshChecking")
    @Unsure
    val serverEnableMeshChecking: Boolean = false,
    @IniProperty("EnableMeshBitingProtection")
    @Unsure
    val enableMeshBitingProtection: Boolean = true,
    @IniProperty("TribeNameChangeCooldown")
    val tribeNameChangeCooldown: Int = 15,
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
    @IniProperty("DinoCharacterFoodDrainMultiplier")
    val dinoCharacterFoodDrainMultiplier: Float = 1.0f,
    @IniProperty("ForceGachaUnhappyInCaves")
    val forceGachaUnhappyInCaves: Boolean = true,
    @IniProperty("DisableImprintDinoBuff")
    val disableImprintDinoBuff: Boolean = false,
    @IniProperty("AllowAnyoneBabyImprintCuddle")
    val allowAnyoneBabyImprintCuddle: Boolean = false,
    @IniProperty("AllowCryoFridgeOnSaddle")
    val allowCryoFridgeOnSaddle: Boolean = false,
    @IniProperty("DisableCryopodEnemyCheck")
    val disableCryopodEnemyCheck: Boolean = false,
    @IniProperty("DisableCryopodFridgeRequirement")
    val disableCryopodFridgeRequirement: Boolean = false,
    @IniProperty("DestroyTamesOverTheSoftTameLimit")
    val destroyTamesOverTheSoftTameLimit: Boolean = false,
    @IniProperty("MaxTamedDinos_SoftTameLimit")
    val maxTamedDinosSoftTameLimit: Int = 5000,
    @IniProperty("MaxTamedDinos_SoftTameLimit_CountdownForDeletionDuration")
    val maxTamedDinosSoftTameLimitCountdownForDeletionDuration: Int = 604800,
    @IniProperty("TamedDinoDamageMultiplier")
    @Unsure
    val tamedDinoDamageMultiplier: Float = 1.0f,
    @IniProperty("TamedDinoResistanceMultiplier")
    @Unsure
    val tamedDinoResistanceMultiplier: Float = 1.0f,
) {
    fun validateAutoSavePeriodMinutes() = autoSavePeriodMinutes >= 0
    fun validateActiveMods() = activeMods.all { it > 0 }
    fun validateActiveMapMod() = activeMapMod == null || activeMapMod > 0
    fun validateDifficultyOffset() = difficultyOffset >= 0.0f
    fun validateStructureMultipliers() = structureResistanceMultiplier >= 0 && structurePreventResourceRadiusMultiplier >= 0 && perPlatformMaxStructuresMultiplier >= 0 && platformSaddleBuildAreaBoundsMultiplier >= 0
    fun validateStructureLimits() = theMaxStructuresInRange >= 0 && maxPlatformSaddleStructureLimit >= 0
    fun validateStructurePickup() = structurePickupTimeAfterPlacement >= 0 && structurePickupHoldDuration >= 0
    fun validateStructureDecay() = pvEStructureDecayPeriodMultiplier >= 0 && autoDestroyOldStructuresMultiplier >= 0
    fun validateItemStack() = itemStackSizeMultiplier >= 0
    fun validateDinoFoodDrain() = dinoCharacterFoodDrainMultiplier >= 0
    fun validateSoftTameLimits() = maxTamedDinosSoftTameLimit >= 0 && maxTamedDinosSoftTameLimitCountdownForDeletionDuration >= 0
    fun validateKickIdlePlayersPeriod() = kickIdlePlayersPeriod >= 0
    fun validateMinimumDinoReuploadInterval() = minimumDinoReuploadInterval == null || minimumDinoReuploadInterval >= 0
    fun validateTributeExpirations() = listOf(
        tributeCharacterExpirationSeconds,
        tributeDinoExpirationSeconds,
        tributeItemExpirationSeconds,
    ).all { it == null || it >= 0 }
    fun validateRconServerGameLogBuffer() = rconServerGameLogBuffer >= 0
    fun validateMaxTributeDinos() = maxTributeDinos >= 20 && maxTributeDinos <= 100
    fun validateMaxTributeItems() = maxTributeItems >= 50 && maxTributeItems <= 100
    fun validateBanListUrl() = banListUrl == null || (banListUrl.isNotBlank() && banListUrl.isValidUrl())
    fun validateBadWordListUrl() = badWordListUrl == null || (badWordListUrl.isNotBlank() && badWordListUrl.isValidUrl())
    fun validateBadWordWhiteListUrl() = badWordWhiteListUrl == null || (badWordWhiteListUrl.isNotBlank() && badWordWhiteListUrl.isValidUrl())
    fun validateAdminListUrl() = adminListUrl == null || (adminListUrl.isNotBlank() && adminListUrl.isValidUrl())
    fun validateCosmeticWhitelistOverride() = cosmeticWhitelistOverride == null || (cosmeticWhitelistOverride.isNotBlank() && cosmeticWhitelistOverride.isValidUrl())
    fun validateUpdateAllowedCheatersInterval() = updateAllowedCheatersInterval >= 0
    fun validateCustomDynamicConfigUrl() = customDynamicConfigUrl == null || (customDynamicConfigUrl.isNotBlank() && customDynamicConfigUrl.isValidUrl())
    fun validateTribeNameChangeCooldown() = tribeNameChangeCooldown >= 0
    fun validate() = validateAutoSavePeriodMinutes() && validateActiveMods() && validateActiveMapMod() && validateDifficultyOffset() && validateStructureMultipliers() && validateStructureLimits() && validateStructurePickup() && validateStructureDecay() && validateItemStack() && validateDinoFoodDrain() && validateSoftTameLimits() && validateKickIdlePlayersPeriod() && validateRconServerGameLogBuffer() && validateMaxTributeDinos() && validateMaxTributeItems() && validateBanListUrl() && validateBadWordListUrl() && validateBadWordWhiteListUrl() && validateAdminListUrl() && validateCosmeticWhitelistOverride() && validateUpdateAllowedCheatersInterval() && validateCustomDynamicConfigUrl() && validateTribeNameChangeCooldown() && validateMinimumDinoReuploadInterval() && validateTributeExpirations()
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

    /**
     * Older profiles stored mods on the launch line. Move them to [ServerSettings.activeMods]
     * when the INI value is still empty; the launch line no longer emits `-mods`.
     */
    fun migrateLegacyMods(legacyMods: List<String>): GameUserSettings {
        val parsed = legacyMods.mapNotNull { it.trim().toIntOrNull() }.filter { it > 0 }
        return if (serverSettings.activeMods.isEmpty() && parsed.isNotEmpty()) {
            copy(serverSettings = serverSettings.copy(activeMods = parsed))
        } else {
            this
        }
    }
}

private val ACTIVE_MODS_LINE = Regex("""(?m)^\s*ActiveMods\s*=\s*(.*?)\s*$""")

/**
 * Compatibility shim for inisaur 0.5.0.
 *
 * Inisaur treats a one-item comma list as a scalar, so `ActiveMods=111` deserializes as an
 * empty list. The sibling library has been fixed to wrap a `Plain` value when a collection is
 * requested; remove this shim once a release containing that fix is published.
 */
fun GameUserSettings.withActiveModsFromRaw(rawText: String): GameUserSettings {
    if (serverSettings.activeMods.isNotEmpty()) return this
    val raw = ACTIVE_MODS_LINE.find(rawText)?.groupValues?.get(1)?.trim() ?: return this
    val ids = raw.removeSurrounding("\"")
        .split(',')
        .mapNotNull { it.trim().toIntOrNull() }
        .filter { it > 0 }
    return if (ids.isNotEmpty()) {
        copy(serverSettings = serverSettings.copy(activeMods = ids))
    } else {
        this
    }
}