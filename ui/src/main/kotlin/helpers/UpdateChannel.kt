package eu.wynq.arkascendedservermanager.ui.helpers

import io.github.z4kn4fein.semver.toVersion
import java.util.prefs.Preferences

/**
 * Update channels exposed in Settings.
 *
 * [STABLE] follows normal GitHub releases only. [RC] additionally checks prerelease
 * tags whose suffix starts with `rc` (for example `v0.18.0-rc1`), and still checks
 * the stable channel so an RC tester is moved onto `v0.18.0` when it ships.
 */
enum class UpdateChannel(
    val storageValue: String,
    val updaterChannel: String,
    val allowPrerelease: Boolean,
) {
    STABLE("stable", "latest", false),
    RC("rc", "rc", true);

    companion object {
        fun fromStorage(value: String?): UpdateChannel =
            entries.firstOrNull { it.storageValue.equals(value, ignoreCase = true) } ?: STABLE
    }
}

/**
 * Lightweight preference store for the update channel.
 *
 * The updater runs before the database is opened, so this cannot live in the
 * normal Settings table without restructuring startup. Preferences are also the
 * right scope for a per-user choice.
 */
object UpdateChannelStore {
    private const val NODE = "eu/wynq/arkascendedservermanager"
    private const val KEY = "updateChannel"

    private val preferences: Preferences by lazy {
        Preferences.userRoot().node(NODE)
    }

    fun current(): UpdateChannel = runCatching {
        UpdateChannel.fromStorage(preferences.get(KEY, UpdateChannel.STABLE.storageValue))
    }.getOrDefault(UpdateChannel.STABLE)

    fun save(channel: UpdateChannel) {
        runCatching {
            preferences.put(KEY, channel.storageValue)
            preferences.flush()
        }
    }
}

/**
 * The RC channel must accept the final stable release even though it is newer
 * than the last RC. Returns true when the stable candidate should win.
 */
fun shouldPreferStable(stableVersion: String?, rcVersion: String?): Boolean {
    val stable = stableVersion?.let { runCatching { it.toVersion(false) }.getOrNull() }
    val rc = rcVersion?.let { runCatching { it.toVersion(false) }.getOrNull() }
    return when {
        stable != null && rc != null -> stable >= rc
        stable != null -> true
        else -> false
    }
}
