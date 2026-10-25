package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniString
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of `ConfigOverrideItemMaxQuantity`:
 *
 * `ConfigOverrideItemMaxQuantity=(ItemClassString="PrimalItemResource_Thatch_C",Quantity=(MaxItemQuantity=100,bIgnoreMultiplier=True))`
 */
@IniStruct
@Serializable
data class StackOverride(
    @IniProperty("ItemClassString")
    @IniString(quoted = true)
    val itemClassString: String = "",
    @IniProperty("Quantity")
    val quantity: StackQuantity = StackQuantity(),
)

@IniStruct
@Serializable
data class StackQuantity(
    @IniProperty("MaxItemQuantity")
    val maxItemQuantity: Int = 1,
    @IniProperty("bIgnoreMultiplier")
    val ignoreMultiplier: Boolean = true,
)
