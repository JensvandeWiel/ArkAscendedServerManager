package eu.wynq.arkascendedservermanager.core.rcon

import eu.wynq.arkascendedservermanager.core.rcon.protocol.SourceRconPacket
import eu.wynq.arkascendedservermanager.core.rcon.protocol.SourceRconPacketType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class RconConfig(
    val host: String,
    val port: Int = 27015,
    val password: String,
    val connectTimeout: Duration = 5.seconds,
    val readTimeout: Duration = 5.seconds,
    val useTerminatorRequest: Boolean = true
)

data class RconCommandResponse(
    val requestId: Int,
    val output: String,
    val packetsReceived: Int
)

sealed class RconException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
class RconAuthenticationException(message: String) : RconException(message)
class RconProtocolException(message: String, cause: Throwable? = null) : RconException(message, cause)
class RconConnectionException(message: String, cause: Throwable? = null) : RconException(message, cause)

class SourceRconClient(
    private val config: RconConfig,
    private val socketFactory: () -> Socket = { Socket() }
) : Closeable {
    private val requestId = AtomicInteger(1)
    private val mutex = Mutex()

    private var socket: Socket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null
    private var authenticated = false

    suspend fun connectAndAuthenticate() {
        connect()
        authenticate()
    }

    suspend fun connect() = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (socket?.isConnected == true && socket?.isClosed == false) return@withLock

            val client = socketFactory()
            try {
                val readTimeoutMillis = config.readTimeout.inWholeMilliseconds
                    .coerceIn(1L, Int.MAX_VALUE.toLong())
                    .toInt()
                val connectTimeoutMillis = config.connectTimeout.inWholeMilliseconds
                    .coerceIn(1L, Int.MAX_VALUE.toLong())
                    .toInt()

                client.tcpNoDelay = true
                client.soTimeout = readTimeoutMillis
                client.connect(InetSocketAddress(config.host, config.port), connectTimeoutMillis)
            } catch (error: Exception) {
                client.close()
                throw RconConnectionException(
                    "Failed to connect to RCON at ${config.host}:${config.port}",
                    error
                )
            }

            socket = client
            input = client.getInputStream()
            output = client.getOutputStream()
            authenticated = false
        }
    }

    suspend fun authenticate(password: String = config.password) = withContext(Dispatchers.IO) {
        require(password.isNotEmpty()) { "RCON password must not be empty" }

        mutex.withLock {
            ensureConnected()

            val authRequestId = nextRequestId()
            writePacket(
                SourceRconPacket(
                    id = authRequestId,
                    type = SourceRconPacketType.SERVERDATA_AUTH,
                    body = password.requireAscii("RCON password")
                )
            )

            // Servers usually respond with an empty RESPONSE_VALUE before AUTH_RESPONSE.
            while (true) {
                val packet = readPacket()
                if (packet.type != SourceRconPacketType.SERVERDATA_AUTH_RESPONSE) {
                    continue
                }

                if (packet.id == -1) {
                    authenticated = false
                    throw RconAuthenticationException("RCON authentication failed")
                }

                if (packet.id != authRequestId) {
                    throw RconProtocolException(
                        "Unexpected auth response id ${packet.id}, expected $authRequestId"
                    )
                }

                authenticated = true
                return@withLock
            }
        }
    }

    suspend fun execute(command: String): RconCommandResponse = withContext(Dispatchers.IO) {
        require(command.isNotBlank()) { "RCON command must not be blank" }

        mutex.withLock {
            ensureConnected()
            ensureAuthenticated()

            val commandRequestId = nextRequestId()
            writePacket(
                SourceRconPacket(
                    id = commandRequestId,
                    type = SourceRconPacketType.SERVERDATA_EXECCOMMAND,
                    body = command.requireAscii("RCON command")
                )
            )

            val terminatorRequestId = if (config.useTerminatorRequest) nextRequestId() else null
            if (terminatorRequestId != null) {
                writePacket(
                    SourceRconPacket(
                        id = terminatorRequestId,
                        type = SourceRconPacketType.SERVERDATA_RESPONSE_VALUE,
                        body = byteArrayOf()
                    )
                )
            }

            val outputBuilder = StringBuilder()
            var packetsReceived = 0
            var response: RconCommandResponse? = null

            while (response == null) {
                val packet = readPacket()
                when {
                    packet.type == SourceRconPacketType.SERVERDATA_RESPONSE_VALUE && packet.id == commandRequestId -> {
                        outputBuilder.append(packet.bodyAsAscii())
                        packetsReceived += 1

                        if (terminatorRequestId == null) {
                            response = RconCommandResponse(
                                requestId = commandRequestId,
                                output = outputBuilder.toString(),
                                packetsReceived = packetsReceived
                            )
                        }
                    }

                    packet.type == SourceRconPacketType.SERVERDATA_RESPONSE_VALUE && packet.id == terminatorRequestId -> {
                        // Marker packet means the command response stream is complete.
                        response = RconCommandResponse(
                            requestId = commandRequestId,
                            output = outputBuilder.toString(),
                            packetsReceived = packetsReceived
                        )
                    }

                    packet.type == SourceRconPacketType.SERVERDATA_AUTH_RESPONSE && packet.id == -1 -> {
                        authenticated = false
                        throw RconAuthenticationException(
                            "RCON authentication expired. Reconnect and authenticate again."
                        )
                    }

                    else -> Unit
                }
            }

            response
                ?: throw RconProtocolException("RCON response stream ended unexpectedly")
        }
    }

    override fun close() {
        authenticated = false
        output = null
        input = null
        socket?.runCatching { close() }
        socket = null
    }

    private fun ensureConnected() {
        if (socket?.isConnected != true || socket?.isClosed == true || input == null || output == null) {
            throw RconConnectionException("RCON client is not connected")
        }
    }

    private fun ensureAuthenticated() {
        if (!authenticated) {
            throw RconAuthenticationException("RCON client is not authenticated")
        }
    }

    private fun readPacket(): SourceRconPacket {
        val inputStream = input ?: throw RconConnectionException("RCON input stream is not available")
        return try {
            SourceRconPacket.readFrom(inputStream)
                ?: throw RconConnectionException("RCON connection was closed by the server")
        } catch (timeout: SocketTimeoutException) {
            throw RconConnectionException("RCON read timed out", timeout)
        } catch (error: IllegalArgumentException) {
            throw RconProtocolException("Malformed RCON packet", error)
        }
    }

    private fun writePacket(packet: SourceRconPacket) {
        val outputStream = output ?: throw RconConnectionException("RCON output stream is not available")
        try {
            outputStream.write(packet.encode())
            outputStream.flush()
        } catch (error: Exception) {
            throw RconConnectionException("Failed to write RCON packet", error)
        }
    }

    private fun nextRequestId(): Int {
        val next = requestId.getAndIncrement()
        if (next <= 0) {
            requestId.set(1)
            return requestId.getAndIncrement()
        }
        return next
    }
}

private fun String.requireAscii(fieldName: String): ByteArray {
    require(all { it.code <= 0x7F }) {
        "$fieldName must contain only ASCII characters"
    }
    return toByteArray(Charsets.US_ASCII)
}

suspend fun <T> withSourceRcon(config: RconConfig, block: suspend SourceRconClient.() -> T): T {
    val client = SourceRconClient(config)
    return client.use {
        it.connectAndAuthenticate()
        it.block()
    }
}
