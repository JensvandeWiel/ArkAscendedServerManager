package support

import io.github.z4kn4fein.semver.Version

/**
 * Windows file versions cannot carry a prerelease suffix. Overseer is built from
 * the app version, so compare only major/minor/patch for installed-wrapper checks.
 */
fun Version.sameMajorMinorPatch(other: Version): Boolean =
    major == other.major && minor == other.minor && patch == other.patch
