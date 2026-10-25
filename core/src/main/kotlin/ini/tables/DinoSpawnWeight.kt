package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniString
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of `DinoSpawnWeightMultipliers`:
 *
 * `DinoSpawnWeightMultipliers=(DinoNameTag="Rex",SpawnWeightMultiplier=1.0,OverrideSpawnLimitPercentage=True,SpawnLimitPercentage=1.0)`
 */
@IniStruct
@Serializable
data class DinoSpawnWeight(
    @IniProperty("DinoNameTag")
    @IniString(quoted = true)
    val dinoNameTag: String = "",
    @IniProperty("SpawnWeightMultiplier")
    val spawnWeightMultiplier: Float = 1.0f,
    @IniProperty("OverrideSpawnLimitPercentage")
    val overrideSpawnLimitPercentage: Boolean = true,
    @IniProperty("SpawnLimitPercentage")
    val spawnLimitPercentage: Float = 1.0f,
)
