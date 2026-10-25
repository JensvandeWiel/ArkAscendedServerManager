package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of an ARK "class multiplier" table, e.g.
 *
 * `DinoClassDamageMultipliers=(ClassName="MegaRex_Character_BP_C",Multiplier=0.1)`
 */
@IniStruct
@Serializable
data class ClassMultiplier(
    @IniProperty("ClassName")
    val className: String = "",
    @IniProperty("Multiplier")
    val multiplier: Float = 1.0f,
)
