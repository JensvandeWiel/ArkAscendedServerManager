package eu.wynq.arkascendedservermanager.core.ini

import IniFile
import annotations.IniSerializable
import annotations.WithIgnored
import kotlinx.serialization.Serializable

@IniSerializable
@Serializable
data class GameUserSettings(
    override val ignoredKeys: IniFile = IniFile()
) : WithIgnored