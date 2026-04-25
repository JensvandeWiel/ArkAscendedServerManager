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
) {
    fun validate() = true
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
