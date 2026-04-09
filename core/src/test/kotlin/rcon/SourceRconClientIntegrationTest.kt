package eu.wynq.arkascendedservermanager.core.rcon

import eu.wynq.arkascendedservermanager.core.rcon.protocol.SourceRconPacket
import eu.wynq.arkascendedservermanager.core.rcon.protocol.SourceRconPacketType
import kotlinx.coroutines.runBlocking
import java.net.ServerSocket
import java.net.Socket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SourceRconClientIntegrationTest {

    @Test
    fun `authenticates and combines multi packet response`() {
        runBlocking {
            FakeSourceRconServer(password = "secret") { command ->
                when (command) {
                    "status" -> listOf("line-1\n", "line-2\n")
                    else -> listOf("unknown\n")
                }
            }.use { server ->
                val client = SourceRconClient(
                    config = RconConfig(
                        host = "127.0.0.1",
                        port = server.port,
                        password = "secret"
                    )
                )

                client.use {
                    it.connectAndAuthenticate()
                    val response = it.execute("status")
                    assertEquals("line-1\nline-2\n", response.output)
                    assertEquals(2, response.packetsReceived)
                }
            }
        }
    }

    @Test
    fun `throws authentication exception for wrong password`() {
        runBlocking {
            FakeSourceRconServer(password = "secret") { listOf("ok") }.use { server ->
                val client = SourceRconClient(
                    config = RconConfig(
                        host = "127.0.0.1",
                        port = server.port,
                        password = "wrong"
                    )
                )

                client.use {
                    assertFailsWith<RconAuthenticationException> {
                        it.connectAndAuthenticate()
                    }
                }
            }
        }
    }
}

private class FakeSourceRconServer(
    private val password: String,
    private val commandHandler: (String) -> List<String>
) : AutoCloseable {
    private val serverSocket = ServerSocket(0)
    private val worker = Thread {
        serverSocket.accept().use { socket ->
            socket.tcpNoDelay = true
            handleClient(socket)
        }
    }

    val port: Int = serverSocket.localPort

    init {
        worker.isDaemon = true
        worker.start()
    }

    override fun close() {
        serverSocket.close()
        worker.join(1_000)
    }

    private fun handleClient(socket: Socket) {
        val input = socket.getInputStream()
        val output = socket.getOutputStream()

        while (true) {
            val packet = runCatching { SourceRconPacket.readFrom(input) }.getOrNull() ?: break
            when (packet.type) {
                SourceRconPacketType.SERVERDATA_AUTH -> {
                    output.write(
                        SourceRconPacket(
                            id = packet.id,
                            type = SourceRconPacketType.SERVERDATA_RESPONSE_VALUE,
                            body = byteArrayOf()
                        ).encode()
                    )

                    val authResponseId = if (packet.bodyAsAscii() == password) packet.id else -1
                    output.write(
                        SourceRconPacket(
                            id = authResponseId,
                            type = SourceRconPacketType.SERVERDATA_AUTH_RESPONSE,
                            body = byteArrayOf()
                        ).encode()
                    )
                    output.flush()
                }

                SourceRconPacketType.SERVERDATA_EXECCOMMAND -> {
                    commandHandler(packet.bodyAsAscii()).forEach { part ->
                        output.write(
                            SourceRconPacket(
                                id = packet.id,
                                type = SourceRconPacketType.SERVERDATA_RESPONSE_VALUE,
                                body = part.toByteArray(Charsets.US_ASCII)
                            ).encode()
                        )
                    }
                    output.flush()
                }

                SourceRconPacketType.SERVERDATA_RESPONSE_VALUE -> {
                    // Emulate the SRCDS terminator trick response packets.
                    output.write(
                        SourceRconPacket(
                            id = packet.id,
                            type = SourceRconPacketType.SERVERDATA_RESPONSE_VALUE,
                            body = byteArrayOf()
                        ).encode()
                    )
                    output.write(
                        SourceRconPacket(
                            id = packet.id,
                            type = SourceRconPacketType.SERVERDATA_RESPONSE_VALUE,
                            body = byteArrayOf(1, 0, 0, 0)
                        ).encode()
                    )
                    output.flush()
                }
            }
        }
    }
}
