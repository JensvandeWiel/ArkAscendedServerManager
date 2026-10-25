package eu.wynq.arkascendedservermanager.core.ini.tables

import annotations.IniProperty
import annotations.IniString
import annotations.IniStruct
import kotlinx.serialization.Serializable

/**
 * One line of `CheatTeleportLocations`:
 *
 * `CheatTeleportLocations=(TeleportName="Base",TeleportLocation=(X=1.0,Y=2.0,Z=3.0))`
 */
@IniStruct
@Serializable
data class TeleportEntry(
    @IniProperty("TeleportName")
    @IniString(quoted = true)
    val teleportName: String = "",
    @IniProperty("TeleportLocation")
    val teleportLocation: TeleportLocation = TeleportLocation(),
)

@IniStruct
@Serializable
data class TeleportLocation(
    @IniProperty("X")
    val x: Float = 0.0f,
    @IniProperty("Y")
    val y: Float = 0.0f,
    @IniProperty("Z")
    val z: Float = 0.0f,
)
