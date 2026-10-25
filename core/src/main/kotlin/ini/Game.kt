package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniArray
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.OmitIfNull
import annotations.WithIgnored
import ArrayType
import eu.wynq.arkascendedservermanager.core.ini.tables.ClassMultiplier
import eu.wynq.arkascendedservermanager.core.ini.tables.DinoSpawnWeight
import eu.wynq.arkascendedservermanager.core.ini.tables.EngramAutoUnlock
import eu.wynq.arkascendedservermanager.core.ini.tables.EngramIndexOverride
import eu.wynq.arkascendedservermanager.core.ini.tables.EngramOverride
import eu.wynq.arkascendedservermanager.core.ini.tables.NpcReplacement
import eu.wynq.arkascendedservermanager.core.ini.tables.StackOverride
import eu.wynq.arkascendedservermanager.core.ini.tables.TeleportEntry
import support.Unsure
import kotlinx.serialization.Serializable

@IniSection("/Script/ShooterGame.ShooterGameMode")
@Serializable
data class ShooterGameMode(
    @IniProperty("GenericXPMultiplier")
    val genericXPMultiplier: Float = 1.0f,
    @IniProperty("CraftXPMultiplier")
    val craftXPMultiplier: Float = 1.0f,
    @IniProperty("HarvestXPMultiplier")
    val harvestXPMultiplier: Float = 1.0f,
    @IniProperty("KillXPMultiplier")
    val killXPMultiplier: Float = 1.0f,
    @IniProperty("SpecialXPMultiplier")
    val specialXPMultiplier: Float = 1.0f,
    @IniProperty("ResourceNoReplenishRadiusPlayers")
    val resourceNoReplenishRadiusPlayers: Float = 1.0f,
    @IniProperty("ResourceNoReplenishRadiusStructures")
    val resourceNoReplenishRadiusStructures: Float = 1.0f,
    @param:Unsure
    @IniProperty("BaseTemperatureMultiplier")
    val baseTemperatureMultiplier: Float = 1.0f,
    @IniProperty("GlobalSpoilingTimeMultiplier")
    val globalSpoilingTimeMultiplier: Float = 1.0f,
    @IniProperty("GlobalItemDecompositionTimeMultiplier")
    val globalItemDecompositionTimeMultiplier: Float = 1.0f,
    @IniProperty("GlobalPoweredBatteryDurabilityDecreasePerSecond")
    @Unsure
    val globalPoweredBatteryDurabilityDecreasePerSecond: Float = 3.0f,
    @IniProperty("FuelConsumptionIntervalMultiplier")
    @Unsure
    val fuelConsumptionIntervalMultiplier: Float = 1.0f,
    @IniProperty("FishingLootQualityMultiplier")
    @Unsure
    val fishingLootQualityMultiplier: Float = 1.0f,
    @IniProperty("SupplyCrateLootQualityMultiplier")
    @Unsure
    val supplyCrateLootQualityMultiplier: Float = 1.0f,
    @param:Unsure
    @IniProperty("GlobalCorpseDecompositionTimeMultiplier")
    val globalCorpseDecompositionTimeMultiplier: Float = 1.0f,
    @IniProperty("CropDecaySpeedMultiplier")
    val cropDecaySpeedMultiplier: Float = 1.0f,
    @IniProperty("CropGrowthSpeedMultiplier")
    val cropGrowthSpeedMultiplier: Float = 1.0f,
    @IniProperty("LayEggIntervalMultiplier")
    val layEggIntervalMultiplier: Float = 1.0f,
    @IniProperty("MatingIntervalMultiplier")
    val matingIntervalMultiplier: Float = 1.0f,
    @IniProperty("MatingSpeedMultiplier")
    val matingSpeedMultiplier: Float = 1.0f,
    @IniProperty("EggHatchSpeedMultiplier")
    val eggHatchSpeedMultiplier: Float = 1.0f,
    @IniProperty("BabyMatureSpeedMultiplier")
    val babyMatureSpeedMultiplier: Float = 1.0f,
    @IniProperty("BabyFoodConsumptionSpeedMultiplier")
    val babyFoodConsumptionSpeedMultiplier: Float = 1.0f,
    @IniProperty("BabyImprintAmountMultiplier")
    val babyImprintAmountMultiplier: Float = 1.0f,
    @IniProperty("BabyImprintingStatScaleMultiplier")
    val babyImprintingStatScaleMultiplier: Float = 1.0f,
    @IniProperty("BabyCuddleIntervalMultiplier")
    val babyCuddleIntervalMultiplier: Float = 1.0f,
    @IniProperty("BabyCuddleGracePeriodMultiplier")
    val babyCuddleGracePeriodMultiplier: Float = 1.0f,
    @IniProperty("BabyCuddleLoseImprintQualitySpeedMultiplier")
    val babyCuddleLoseImprintQualitySpeedMultiplier: Float = 1.0f,
    @IniProperty("DestroyTamesOverLevelClamp")
    val destroyTamesOverLevelClamp: Int = 0,
    @IniProperty("bUseDinoLevelUpAnimations")
    val useDinoLevelUpAnimations: Boolean = true,
    @IniProperty("PoopIntervalMultiplier")
    val poopIntervalMultiplier: Float = 1.0f,
    @IniProperty("HairGrowthSpeedMultiplier")
    val hairGrowthSpeedMultiplier: Float = 0.0f,
    @IniProperty("bDisableFriendlyFire")
    val disableFriendlyFire: Boolean = false,
    @IniProperty("bPvEDisableFriendlyFire")
    val pvEDisableFriendlyFire: Boolean = false,
    @IniProperty("bDisableLootCrates")
    @Unsure
    val disableLootCrates: Boolean = false,
    @IniProperty("bUseSingleplayerSettings")
    val useSingleplayerSettings: Boolean = false,
    @IniProperty("bShowCreativeMode")
    val showCreativeMode: Boolean = false,
    @IniProperty("bAllowUnlimitedRespecs")
    val allowUnlimitedRespecs: Boolean = false,
    @IniProperty("bAllowSpeedLeveling")
    val allowSpeedLeveling: Boolean = false,
    @IniProperty("bAllowFlyerSpeedLeveling")
    val allowFlyerSpeedLeveling: Boolean = false,
    @IniProperty("bIgnoreStructuresPreventionVolumes")
    val ignoreStructuresPreventionVolumes: Boolean = false,
    @IniProperty("bDisableStructurePlacementCollision")
    val disableStructurePlacementCollision: Boolean = false,
    @IniProperty("bDisablePhotoMode")
    val disablePhotoMode: Boolean = false,
    @IniProperty("PhotoModeRangeLimit")
    val photoModeRangeLimit: Int = 3000,
    @IniProperty("TribeSlotReuseCooldown")
    @Unsure
    val tribeSlotReuseCooldown: Int = 0,
    @IniProperty("MaxNumberOfPlayersInTribe")
    @Unsure
    val maxNumberOfPlayersInTribe: Int = 0,
    @param:Unsure
    @IniProperty("MaxAlliancesPerTribe")
    val maxAlliancesPerTribe: Int = 0,
    @param:Unsure
    @IniProperty("MaxTribesPerAlliance")
    val maxTribesPerAlliance: Int = 0,
    @param:Unsure
    @IniProperty("bPvEAllowTribeWar")
    val pveAllowTribeWar: Boolean = true,
    @param:Unsure
    @IniProperty("bPvEAllowTribeWarCancel")
    val pveAllowTribeWarCancel: Boolean = false,
    @IniProperty("bAllowCustomRecipes")
    @Unsure
    val allowCustomRecipes: Boolean = true,
    @IniProperty("CustomRecipeEffectivenessMultiplier")
    val customRecipeEffectivenessMultiplier: Float = 1.0f,
    @IniProperty("CustomRecipeSkillMultiplier")
    val customRecipeSkillMultiplier: Float = 1.0f,
    @IniProperty("bDisableWirelessCrafting")
    val disableWirelessCrafting: Boolean = false,
    @IniProperty("bDisableWirelessCraftingForDinos")
    val disableWirelessCraftingForDinos: Boolean = false,
    @IniProperty("bDisableWirelessCraftingForPlayers")
    val disableWirelessCraftingForPlayers: Boolean = false,
    @IniProperty("bDisableWirelessCraftingForStructures")
    val disableWirelessCraftingForStructures: Boolean = false,
    @IniProperty("WirelessCraftingRangeOverride")
    val wirelessCraftingRangeOverride: Int = 3000,
    @IniProperty("LimitGeneratorsNum")
    val limitGeneratorsNum: Int = 3,
    @IniProperty("LimitGeneratorsRange")
    val limitGeneratorsRange: Int = 15000,
    @IniProperty("bHardLimitTurretsInRange")
    @Unsure
    val hardLimitTurretsInRange: Boolean = false,
    @IniProperty("bLimitTurretsInRange")
    @Unsure
    val limitTurretsInRange: Boolean = true,
    @IniProperty("LimitTurretsNum")
    @Unsure
    val limitTurretsNum: Int = 100,
    @IniProperty("LimitTurretsRange")
    @Unsure
    val limitTurretsRange: Float = 10000.0f,
    @param:Unsure
    @IniProperty("PlayerHarvestingDamageMultiplier")
    val playerHarvestingDamageMultiplier: Float = 1.0f,
    @IniProperty("CraftingSkillBonusMultiplier")
    val craftingSkillBonusMultiplier: Float = 1.0f,
    @IniProperty("MaxFallSpeedMultiplier")
    val maxFallSpeedMultiplier: Float = 1.0f,
    @param:Unsure
    @IniProperty("PlayerBaseStatMultipliers")
    @OmitIfNull
    val playerBaseStatMultipliers: Map<Int, Float>? = null,
    @IniProperty("ItemStatClamps")
    @OmitIfNull
    @Unsure
    val itemStatClamps: Map<Int, Int>? = null,
    @IniProperty("MutagenLevelBoost")
    @OmitIfNull
    @Unsure
    val mutagenLevelBoost: Map<Int, Int>? = null,
    @IniProperty("MutagenLevelBoost_Bred")
    @OmitIfNull
    @Unsure
    val mutagenLevelBoostBred: Map<Int, Int>? = null,
    @IniProperty("PerLevelStatsMultiplier_Player")
    @OmitIfNull
    val perLevelStatsMultiplierPlayer: Map<Int, Float>? = null,
    @IniProperty("DinoHarvestingDamageMultiplier")
    @Unsure
    val dinoHarvestingDamageMultiplier: Float = 3.2f,
    @IniProperty("DinoTurretDamageMultiplier")
    @Unsure
    val dinoTurretDamageMultiplier: Float = 1.0f,
    @IniProperty("DinoClassDamageMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val dinoClassDamageMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("DinoClassResistanceMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val dinoClassResistanceMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("TamedDinoClassDamageMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val tamedDinoClassDamageMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("TamedDinoClassResistanceMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val tamedDinoClassResistanceMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("TamedDinoClassSpeedMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val tamedDinoClassSpeedMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("TamedDinoClassStaminaMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val tamedDinoClassStaminaMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("HarvestResourceItemAmountClassMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    val harvestResourceItemAmountClassMultipliers: List<ClassMultiplier> = emptyList(),
    @IniProperty("DinoSpawnWeightMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val dinoSpawnWeightMultipliers: List<DinoSpawnWeight> = emptyList(),
    @IniProperty("NPCReplacements")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val npcReplacements: List<NpcReplacement> = emptyList(),
    @IniProperty("PreventBreedingForClassNames")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    val preventBreedingForClassNames: List<String> = emptyList(),
    @IniProperty("PreventDinoTameClassNames")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val preventDinoTameClassNames: List<String> = emptyList(),
    @IniProperty("OverrideNamedEngramEntries")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    val overrideNamedEngramEntries: List<EngramOverride> = emptyList(),
    @IniProperty("OverrideEngramEntries")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val overrideEngramEntries: List<EngramIndexOverride> = emptyList(),
    @IniProperty("EngramEntryAutoUnlocks")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val engramEntryAutoUnlocks: List<EngramAutoUnlock> = emptyList(),
    @IniProperty("CheatTeleportLocations")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    val cheatTeleportLocations: List<TeleportEntry> = emptyList(),
    @IniProperty("ConfigOverrideItemMaxQuantity")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val configOverrideItemMaxQuantity: List<StackOverride> = emptyList(),
    @IniProperty("OverridePlayerLevelEngramPoints")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    @Unsure
    val overridePlayerLevelEngramPoints: List<Int> = emptyList(),
    @IniProperty("ExcludeItemIndices")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    val excludeItemIndices: List<Int> = emptyList(),
    @IniProperty("PassiveTameIntervalMultiplier")
    @Unsure
    val passiveTameIntervalMultiplier: Float = 1.0f,
    @IniProperty("TamedDinoCharacterFoodDrainMultiplier")
    @Unsure
    val tamedDinoCharacterFoodDrainMultiplier: Float = 1.0f,
    @IniProperty("TamedDinoTorporDrainMultiplier")
    @Unsure
    val tamedDinoTorporDrainMultiplier: Float = 1.0f,
    @IniProperty("WildDinoCharacterFoodDrainMultiplier")
    val wildDinoCharacterFoodDrainMultiplier: Float = 1.0f,
    @IniProperty("WildDinoTorporDrainMultiplier")
    @Unsure
    val wildDinoTorporDrainMultiplier: Float = 1.0f,
    @IniProperty("PerLevelStatsMultiplier_DinoTamed")
    @Unsure
    @OmitIfNull
    val perLevelStatsMultiplierDinoTamed: Map<Int, Float>? = null,
    @IniProperty("PerLevelStatsMultiplier_DinoTamed_Add")
    @Unsure
    @OmitIfNull
    val perLevelStatsMultiplierDinoTamedAdd: Map<Int, Float>? = null,
    @IniProperty("PerLevelStatsMultiplier_DinoTamed_Affinity")
    @Unsure
    @OmitIfNull
    val perLevelStatsMultiplierDinoTamedAffinity: Map<Int, Float>? = null,
    @IniProperty("PerLevelStatsMultiplier_DinoWild")
    @Unsure
    @OmitIfNull
    val perLevelStatsMultiplierDinoWild: Map<Int, Float>? = null,
) {
    companion object {
        fun createPlayerBaseStatMultipliersDefault() = mapOf(0 to 1.0f, 1 to 1.0f, 2 to 1.0f, 3 to 1.0f, 4 to 1.0f, 5 to 1.0f, 6 to 1.0f, 7 to 1.0f, 8 to 1.0f, 9 to 1.0f, 10 to 1.0f, 11 to 1.0f)
        fun createPerLevelStatsMultiplierPlayerDefault() = mapOf(0 to 1.0f, 1 to 1.0f, 2 to 1.0f, 3 to 1.0f, 4 to 1.0f, 5 to 1.0f, 6 to 1.0f, 7 to 1.0f, 8 to 1.0f, 9 to 1.0f, 10 to 1.0f, 11 to 1.0f)

        fun createPerLevelStatsMultiplierDinoTamedDefault() = mapOf(0 to 0.2f, 1 to 1.0f, 2 to 1.0f, 3 to 1.0f, 4 to 1.0f, 5 to 1.0f, 6 to 1.0f, 7 to 1.0f, 8 to 0.17f, 9 to 1.0f, 10 to 1.0f, 11 to 1.0f)
        fun createPerLevelStatsMultiplierDinoTamedAddDefault() = mapOf(0 to 0.14f, 1 to 1.0f, 2 to 1.0f, 3 to 1.0f, 4 to 1.0f, 5 to 1.0f, 6 to 1.0f, 7 to 1.0f, 8 to 0.14f, 9 to 1.0f, 10 to 1.0f, 11 to 1.0f)
        fun createPerLevelStatsMultiplierDinoTamedAffinityDefault() = mapOf(0 to 0.44f, 1 to 1.0f, 2 to 1.0f, 3 to 1.0f, 4 to 1.0f, 5 to 1.0f, 6 to 1.0f, 7 to 1.0f, 8 to 0.44f, 9 to 1.0f, 10 to 1.0f, 11 to 1.0f)
        fun createPerLevelStatsMultiplierDinoWildDefault() = mapOf(0 to 1.0f, 1 to 1.0f, 2 to 1.0f, 3 to 1.0f, 4 to 1.0f, 5 to 1.0f, 6 to 1.0f, 7 to 1.0f, 8 to 1.0f, 9 to 1.0f, 10 to 1.0f, 11 to 1.0f)
    }
    fun validateMaxNumberOfPlayersInTribe() = maxNumberOfPlayersInTribe >= 0
    fun validatePhotoModeRangeLimit() = photoModeRangeLimit >= 0
    fun validateWirelessCrafting() = wirelessCraftingRangeOverride >= 0
    fun validateGenerators() = limitGeneratorsNum >= 0 && limitGeneratorsRange >= 0
    fun validateTurrets() = limitTurretsNum >= 0 && limitTurretsRange >= 0
    fun validateBreedingMultipliers() = listOf(
        matingIntervalMultiplier,
        matingSpeedMultiplier,
        eggHatchSpeedMultiplier,
        babyMatureSpeedMultiplier,
        babyFoodConsumptionSpeedMultiplier,
        babyImprintAmountMultiplier,
        babyImprintingStatScaleMultiplier,
        babyCuddleIntervalMultiplier,
        babyCuddleGracePeriodMultiplier,
        babyCuddleLoseImprintQualitySpeedMultiplier,
    ).all { it >= 0 }
    fun validateDestroyTamesOverLevelClamp() = destroyTamesOverLevelClamp >= 0
    fun validateEnvironmentExtras() = listOf(
        globalPoweredBatteryDurabilityDecreasePerSecond,
        fuelConsumptionIntervalMultiplier,
        fishingLootQualityMultiplier,
        supplyCrateLootQualityMultiplier,
    ).all { it >= 0 }
    fun validateClassMultipliers() = listOf(
        dinoClassDamageMultipliers,
        dinoClassResistanceMultipliers,
        tamedDinoClassDamageMultipliers,
        tamedDinoClassResistanceMultipliers,
        tamedDinoClassSpeedMultipliers,
        tamedDinoClassStaminaMultipliers,
        harvestResourceItemAmountClassMultipliers,
    ).all { rows ->
        rows.all { it.className.isNotBlank() && it.multiplier >= 0 } &&
                rows.map { it.className }.distinct().size == rows.size
    }
    fun validateDinoSpawnWeights() = dinoSpawnWeightMultipliers.all {
        it.dinoNameTag.isNotBlank() && it.spawnWeightMultiplier >= 0 && it.spawnLimitPercentage >= 0
    } && dinoSpawnWeightMultipliers.map { it.dinoNameTag.lowercase() }.distinct().size == dinoSpawnWeightMultipliers.size
    fun validateNpcReplacements() = npcReplacements.all { it.fromClassName.isNotBlank() } &&
            npcReplacements.map { it.fromClassName.lowercase() }.distinct().size == npcReplacements.size
    fun validateEngramOverrides() = overrideNamedEngramEntries.all {
        it.engramClassName.isNotBlank() && it.engramPointsCost >= 0 && it.engramLevelRequirement >= 0
    } && overrideNamedEngramEntries.map { it.engramClassName.lowercase() }.distinct().size == overrideNamedEngramEntries.size
    fun validateEngramAutoUnlocks() = engramEntryAutoUnlocks.all {
        it.engramClassName.isNotBlank() && it.levelToAutoUnlock >= 0
    } && engramEntryAutoUnlocks.map { it.engramClassName.lowercase() }.distinct().size == engramEntryAutoUnlocks.size
    fun validateIndexedEngramOverrides() = overrideEngramEntries.all {
        it.engramIndex >= 0 && it.engramPointsCost >= 0 && it.engramLevelRequirement >= 0
    } && overrideEngramEntries.map { it.engramIndex }.distinct().size == overrideEngramEntries.size
    fun validateClassLists() = listOf(preventBreedingForClassNames, preventDinoTameClassNames).all { classes ->
        classes.all { it.isNotBlank() } && classes.map { it.lowercase() }.distinct().size == classes.size
    }
    fun validateItemStatClamps() = itemStatClamps?.let { clamps ->
        clamps.keys.all { it in 0..7 } && clamps.values.all { it >= 0 }
    } ?: true
    fun validateMutagenLevelBoosts() = listOf(mutagenLevelBoost, mutagenLevelBoostBred).all { boosts ->
        boosts == null || (boosts.keys.all { it in 0..11 } && boosts.values.all { it >= 0 })
    }
    fun validateTeleports() = cheatTeleportLocations.all {
        it.teleportName.isNotBlank() &&
                !it.teleportLocation.x.isNaN() && !it.teleportLocation.y.isNaN() && !it.teleportLocation.z.isNaN()
    } && cheatTeleportLocations.map { it.teleportName.lowercase() }.distinct().size == cheatTeleportLocations.size
    fun validateStackOverrides() = configOverrideItemMaxQuantity.all {
        it.itemClassString.isNotBlank() && it.quantity.maxItemQuantity >= 1
    } && configOverrideItemMaxQuantity.map { it.itemClassString.lowercase() }.distinct().size == configOverrideItemMaxQuantity.size
    fun validateLevelEngramPoints() = overridePlayerLevelEngramPoints.all { it >= 0 }
    fun validateExcludeItemIndices() = excludeItemIndices.all { it >= 0 } &&
            excludeItemIndices.distinct().size == excludeItemIndices.size
    fun validateTables() = validateDinoSpawnWeights() && validateNpcReplacements() &&
            validateEngramOverrides() && validateEngramAutoUnlocks() && validateIndexedEngramOverrides() &&
            validateClassLists() && validateItemStatClamps() && validateMutagenLevelBoosts() &&
            validateTeleports() && validateStackOverrides() && validateLevelEngramPoints() &&
            validateExcludeItemIndices()
    fun validateTribeSlotReuseCooldown() = tribeSlotReuseCooldown >= 0
    fun validateMaxAlliancesPerTribe() = maxAlliancesPerTribe >= 0
    fun validateMaxTribesPerAlliance() = maxTribesPerAlliance >= 0
    fun validate() = validateMaxNumberOfPlayersInTribe() && validatePhotoModeRangeLimit() && validateWirelessCrafting() && validateGenerators() && validateTurrets() && validateBreedingMultipliers() && validateDestroyTamesOverLevelClamp() && validateEnvironmentExtras() && validateClassMultipliers() && validateTables() && validateTribeSlotReuseCooldown()
            && validateMaxAlliancesPerTribe() && validateMaxTribesPerAlliance()
}

@IniSerializable
@Serializable
data class Game(
    override val ignoredKeys: IniFile = IniFile(),
    @IniSection
    val shooterGameMode: ShooterGameMode = ShooterGameMode(),
): WithIgnored {
    companion object {
        fun createForNewServer() = Game()
    }
    fun validate() = shooterGameMode.validate()
}