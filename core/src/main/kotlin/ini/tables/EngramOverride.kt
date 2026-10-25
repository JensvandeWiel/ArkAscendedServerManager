package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniString
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of `OverrideNamedEngramEntries`:
 *
 * `OverrideNamedEngramEntries=(EngramClassName="EngramEntry_StoneHatchet_C",EngramHidden=False,EngramPointsCost=5,EngramLevelRequirement=1,RemoveEngramPreReq=False)`
 */
@IniStruct
@Serializable
data class EngramOverride(
    @IniProperty("EngramClassName")
    @IniString(quoted = true)
    val engramClassName: String = "",
    @IniProperty("EngramHidden")
    val engramHidden: Boolean = false,
    @IniProperty("EngramPointsCost")
    val engramPointsCost: Int = 0,
    @IniProperty("EngramLevelRequirement")
    val engramLevelRequirement: Int = 0,
    @IniProperty("RemoveEngramPreReq")
    val removeEngramPreReq: Boolean = false,
)
