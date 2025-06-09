@file:OptIn(ExperimentalUuidApi::class, ExperimentalAtomicApi::class)

package server

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.uuid.ExperimentalUuidApi

class ServerManager(
    private val _profile: ServerProfile,
    private val installManager: InstallManager = InstallManager(_profile),
    private val powerManager: PowerManager = PowerManager(_profile),
) {
    private val logger = KotlinLogging.logger("ServerManager (${_profile.uuid})")

    /**
     * Returns the InstallManager instance for direct access to installation functionality
     */
    fun getInstallManager(): InstallManager {
        return installManager
    }

    /**
     * Returns the PowerManager instance for direct access to power management functionality
     */
    fun getPowerManager(): PowerManager {
        return powerManager
    }

}