package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
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
    @Unsure
    @IniProperty("BaseTemperatureMultiplier")
    val baseTemperatureMultiplier: Float = 1.0f,
    @IniProperty("GlobalSpoilingTimeMultiplier")
    val globalSpoilingTimeMultiplier: Float = 1.0f,
    @IniProperty("GlobalItemDecompositionTimeMultiplier")
    val globalItemDecompositionTimeMultiplier: Float = 1.0f,
    @Unsure
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
    @IniProperty("bAllowCustomRecipes")
    val allowCustomRecipes: Boolean = true,
    @IniProperty("CustomRecipeEffectivenessMultiplier")
    val customRecipeEffectivenessMultiplier: Float = 1.0f,
    @IniProperty("CustomRecipeSkillMultiplier")
    val customRecipeSkillMultiplier: Float = 1.0f,
) {
    fun validateMaxNumberOfPlayersInTribe() = maxNumberOfPlayersInTribe >= 0
    fun validateTribeSlotReuseCooldown() = tribeSlotReuseCooldown >= 0
    fun validate() = validateMaxNumberOfPlayersInTribe() && validateTribeSlotReuseCooldown()
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