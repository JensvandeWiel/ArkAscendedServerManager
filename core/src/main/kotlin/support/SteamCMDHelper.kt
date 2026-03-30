package eu.wynq.arkascendedservermanager.core.support

import Installer
import eu.wynq.arkascendedservermanager.core.db.models.Settings
import java.nio.file.Path

class SteamCMDHelper(val settings: Settings) {
    fun installSteamCMD(): Result<Boolean> {
        val installer = Installer(Path.of(settings.steamcmdPath))

        return installer.install()
    }
}