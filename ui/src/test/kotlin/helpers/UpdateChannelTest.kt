package eu.wynq.arkascendedservermanager.ui.helpers

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpdateChannelTest {

    @Test
    fun `stable final release wins over the latest rc`() {
        assertTrue(shouldPreferStable("0.18.0", "0.18.0-rc2"))
    }

    @Test
    fun `rc is used when it is newer than stable`() {
        assertFalse(shouldPreferStable("0.17.0", "0.18.0-rc1"))
    }

    @Test
    fun `stable patch release wins over its rc`() {
        assertTrue(shouldPreferStable("0.8.1", "0.8.1-rc1"))
    }

    @Test
    fun `later rc is newer than an earlier stable base`() {
        assertFalse(shouldPreferStable("0.8.0", "0.8.1-rc1"))
    }

    @Test
    fun `stable is preferred when rc is unavailable`() {
        assertTrue(shouldPreferStable("0.18.0", null))
    }

    @Test
    fun `rc is preferred when stable is unavailable`() {
        assertFalse(shouldPreferStable(null, "0.18.0-rc1"))
    }

    @Test
    fun `channel storage values are stable`() {
        assertTrue(UpdateChannel.fromStorage("rc") == UpdateChannel.RC)
        assertTrue(UpdateChannel.fromStorage("stable") == UpdateChannel.STABLE)
        assertTrue(UpdateChannel.fromStorage("nonsense") == UpdateChannel.STABLE)
    }
}
