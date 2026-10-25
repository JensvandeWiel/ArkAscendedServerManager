package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniString
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of `NPCReplacements`:
 *
 * `NPCReplacements=(FromClassName="Rex_Character_BP_C",ToClassName="")`
 *
 * An empty [toClassName] removes the spawn instead of replacing it.
 */
@IniStruct
@Serializable
data class NpcReplacement(
    @IniProperty("FromClassName")
    @IniString(quoted = true)
    val fromClassName: String = "",
    @IniProperty("ToClassName")
    @IniString(quoted = true)
    val toClassName: String = "",
)
