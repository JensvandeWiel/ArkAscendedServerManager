package eu.wynq.arkascendedservermanager.core.rcon.protocol

import java.io.EOFException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal data class SourceRconPacket(
    val id: Int,
    val type: Int,
    val body: ByteArray
) {
    fun bodyAsAscii(): String = body.toString(Charsets.US_ASCII)

    fun encode(): ByteArray {
        val packetSize = body.size + MIN_PACKET_SIZE
        require(packetSize <= MAX_PACKET_SIZE) {
            "RCON packet body is too large: ${body.size} bytes"
        }

        val buffer = ByteBuffer
            .allocate(packetSize + Int.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)

        buffer.putInt(packetSize)
        buffer.putInt(id)
        buffer.putInt(type)
        buffer.put(body)
        buffer.put(0)
        buffer.put(0)

        return buffer.array()
    }

    companion object {
        const val MIN_PACKET_SIZE = 10
        const val MAX_PACKET_SIZE = 4096

        fun readFrom(input: InputStream): SourceRconPacket? {
            val packetSize = readInt32LEOrNull(input) ?: return null
            require(packetSize in MIN_PACKET_SIZE..MAX_PACKET_SIZE) {
                "Invalid RCON packet size: $packetSize"
            }

            val payload = readExactly(input, packetSize)
            val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)

            val id = buffer.int
            val type = buffer.int
            val remaining = ByteArray(packetSize - (Int.SIZE_BYTES * 2))
            buffer.get(remaining)

            val bodyTerminatorIndex = findByte(remaining, value = 0.toByte(), startIndex = 0)
            require(bodyTerminatorIndex >= 0) {
                "Malformed RCON packet: body is not null-terminated"
            }

            val secondStringTerminator = findByte(
                remaining,
                value = 0.toByte(),
                startIndex = bodyTerminatorIndex + 1
            )
            require(secondStringTerminator >= 0) {
                "Malformed RCON packet: second string terminator is missing"
            }

            val body = remaining.copyOfRange(0, bodyTerminatorIndex)
            return SourceRconPacket(id = id, type = type, body = body)
        }

        private fun readExactly(input: InputStream, byteCount: Int): ByteArray {
            val bytes = ByteArray(byteCount)
            var offset = 0

            while (offset < byteCount) {
                val read = input.read(bytes, offset, byteCount - offset)
                if (read < 0) {
                    throw EOFException("Stream ended while reading RCON packet")
                }
                offset += read
            }

            return bytes
        }

        private fun readInt32LEOrNull(input: InputStream): Int? {
            val bytes = ByteArray(Int.SIZE_BYTES)
            val firstByte = input.read()
            if (firstByte < 0) return null

            bytes[0] = firstByte.toByte()
            for (index in 1 until Int.SIZE_BYTES) {
                val read = input.read()
                if (read < 0) {
                    throw EOFException("Stream ended while reading RCON packet size")
                }
                bytes[index] = read.toByte()
            }

            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).int
        }

        private fun findByte(bytes: ByteArray, value: Byte, startIndex: Int): Int {
            for (index in startIndex until bytes.size) {
                if (bytes[index] == value) return index
            }
            return -1
        }
    }
}

internal object SourceRconPacketType {
    const val SERVERDATA_RESPONSE_VALUE = 0
    const val SERVERDATA_AUTH_RESPONSE = 2
    const val SERVERDATA_EXECCOMMAND = 2
    const val SERVERDATA_AUTH = 3
}
