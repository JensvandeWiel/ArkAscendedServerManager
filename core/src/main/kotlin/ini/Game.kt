package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.OmitIfNull
import annotations.WithIgnored
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
    @param:Unsure
    @IniProperty("GlobalCorpseDecompositionTimeMultiplier")
    val globalCorpseDecompositionTimeMultiplier: Float = 1.0f,
    @IniProperty("CropDecaySpeedMultiplier")
    val cropDecaySpeedMultiplier: Float = 1.0f,
    @IniProperty("CropGrowthSpeedMultiplier")
    val cropGrowthSpeedMultiplier: Float = 1.0f,
    @IniProperty("LayEggIntervalMultiplier")
    val layEggIntervalMultiplier: Float = 1.0f,
    @IniProperty("PoopIntervalMultiplier")
    val poopIntervalMultiplier: Float = 1.0f,
    @IniProperty("HairGrowthSpeedMultiplier")
    val hairGrowthSpeedMultiplier: Float = 1.0f,
    @IniProperty("bDisableFriendlyFire")
    val disableFriendlyFire: Boolean = false,
    @IniProperty("bPvEDisableFriendlyFire")
    val pvEDisableFriendlyFire: Boolean = false,
    @IniProperty("bDisableLootCrates")
    val disableLootCrates: Boolean = false,
    @IniProperty("bUseSingleplayerSettings")
    val useSingleplayerSettings: Boolean = false,
    @IniProperty("bShowCreativeMode")
    val showCreativeMode: Boolean = false,
    @IniProperty("TribeSlotReuseCooldown")
    val tribeSlotReuseCooldown: Int = 0,
    @IniProperty("MaxNumberOfPlayersInTribe")
    val maxNumberOfPlayersInTribe: Int = 70,
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
    val allowCustomRecipes: Boolean = true,
    @IniProperty("CustomRecipeEffectivenessMultiplier")
    val customRecipeEffectivenessMultiplier: Float = 1.0f,
    @IniProperty("CustomRecipeSkillMultiplier")
    val customRecipeSkillMultiplier: Float = 1.0f,
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
    @IniProperty("PerLevelStatsMultiplier_Player")
    @OmitIfNull
    val perLevelStatsMultiplierPlayer: Map<Int, Float>? = null,
    @IniProperty("DinoHarvestingDamageMultiplier")
    @Unsure
    val dinoHarvestingDamageMultiplier: Float = 3.2f,
    @IniProperty("DinoTurretDamageMultiplier")
    @Unsure
    val dinoTurretDamageMultiplier: Float = 1.0f,
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
    fun validateTribeSlotReuseCooldown() = tribeSlotReuseCooldown >= 0
    fun validateMaxAlliancesPerTribe() = maxAlliancesPerTribe >= 0
    fun validateMaxTribesPerAlliance() = maxTribesPerAlliance >= 0
    fun validate() = validateMaxNumberOfPlayersInTribe() && validateTribeSlotReuseCooldown()
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