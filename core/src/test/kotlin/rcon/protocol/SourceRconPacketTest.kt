package eu.wynq.arkascendedservermanager.core.rcon.protocol

import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SourceRconPacketTest {

    @Test
    fun `encodes and decodes packet roundtrip`() {
        val original = SourceRconPacket(
            id = 7,
            type = SourceRconPacketType.SERVERDATA_EXECCOMMAND,
            body = "status".toByteArray(Charsets.US_ASCII)
        )

        val decoded = SourceRconPacket.readFrom(ByteArrayInputStream(original.encode()))

        assertEquals(7, decoded?.id)
        assertEquals(SourceRconPacketType.SERVERDATA_EXECCOMMAND, decoded?.type)
        assertEquals("status", decoded?.bodyAsAscii())
    }

    @Test
    fun `rejects invalid packet size`() {
        val tooSmallPacket = byteArrayOf(
            0x01, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00
        )

        assertFailsWith<IllegalArgumentException> {
            SourceRconPacket.readFrom(ByteArrayInputStream(tooSmallPacket))
        }
    }
}

