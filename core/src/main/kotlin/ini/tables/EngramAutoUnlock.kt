package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniString
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of `EngramEntryAutoUnlocks`:
 *
 * `EngramEntryAutoUnlocks=(EngramClassName="EngramEntry_StoneHatchet_C",LevelToAutoUnlock=1)`
 */
@IniStruct
@Serializable
data class EngramAutoUnlock(
    @IniProperty("EngramClassName")
    @IniString(quoted = true)
    val engramClassName: String = "",
    @IniProperty("LevelToAutoUnlock")
    val levelToAutoUnlock: Int = 0,
)
