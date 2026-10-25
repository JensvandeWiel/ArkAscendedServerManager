package managers

import eu.wynq.arkascendedservermanager.core.managers.AsaApiInstallManager
import io.github.z4kn4fein.semver.toVersion
import support.sameMajorMinorPatch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VersionTest {

    @Test
    fun `AsaApi display keeps leading zeros from the tag`() {
        assertEquals("2.03.0", AsaApiInstallManager.displayAsaApiVersion("2.03"))
        assertEquals("2.03.0", AsaApiInstallManager.displayAsaApiVersion("v2.03"))
        assertEquals("2.03.1", AsaApiInstallManager.displayAsaApiVersion("2.03.1"))
    }

    @Test
    fun `Overseer compares base version so rc builds are not always outdated`() {
        val rc = "0.8.0-rc1".toVersion(false)
        val final = "0.8.0".toVersion(false)

        assertTrue(rc.sameMajorMinorPatch(final))
        assertFalse("0.8.1-rc1".toVersion(false).sameMajorMinorPatch(final))
    }
}
