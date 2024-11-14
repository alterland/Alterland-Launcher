package ru.alterland.launchercore.data.repository

import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.xbill.DNS.Lookup
import org.xbill.DNS.SRVRecord
import org.xbill.DNS.Type
import ru.alterland.launchercore.data.source.network.model.response.ServerPingResult
import ru.alterland.launchercore.domain.model.PingOptions
import ru.alterland.launchercore.domain.model.ServerPong
import ru.alterland.launchercore.domain.model.ServerStatus
import ru.alterland.launchercore.domain.repository.ServerRepository
import ru.alterland.launchercore.util.*
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.system.measureTimeMillis


class ServerRepositoryImpl(
    private val json: Json
): ServerRepository {

    private val cachedConnections = mutableMapOf<String, Socket>()
    private val cachedImages = mutableMapOf<String, ByteArray>()

    override fun ping(hostname: String, port: Int?): ServerPong =
        ping(PingOptions(hostname = hostname, port = port ?: DEFAULT_PORT))

    @OptIn(ExperimentalEncodingApi::class)
    override fun ping(options: PingOptions): ServerPong {

        try {
            val connection = cachedConnections["${options.hostname}:${options.port}"] ?: run {
                var targetHostname = options.hostname
                var targetPort = options.port
                val records = Lookup(String.format(SRV_QUERY_PREFIX, options.hostname), Type.SRV).run()
                if (records != null) {
                    for (record in records) {
                        val srv = record as SRVRecord
                        targetHostname = srv.target.toString().replaceFirst("\\.$", "")
                        targetPort = srv.port
                    }
                }
                Socket().apply {
                    connect(InetSocketAddress(targetHostname, targetPort), options.timeout)
                    cachedConnections["${options.hostname}:${options.port}"] = this
                }
            }

            val inputStream = DataInputStream(connection.getInputStream())
            val outputStream = DataOutputStream(connection.getOutputStream())

            //> Handshake
            val handshakeBytes = ByteArrayOutputStream()
            val handshake = DataOutputStream(handshakeBytes)

            handshake.writeByte(PACKET_HANDSHAKE)
            handshake.writeVarInt(options.protocolVersion)
            handshake.writeVarInt(options.hostname.length)
            handshake.writeBytes(options.hostname)
            handshake.writeShort(options.port)
            handshake.writeVarInt(1)

            outputStream.writeVarInt(handshakeBytes.size())
            outputStream.write(handshakeBytes.toByteArray())

            //> Status request
            outputStream.writeByte(0x01) // Size of packet
            outputStream.writeByte(PACKET_STATUS_REQUEST)

            //< Status response
            inputStream.readVarInt() // Size

            var id = inputStream.readVarInt()

            if (id == -1) throw Exception("Server prematurely ended stream")
            //if (id != PACKET_STATUS_REQUEST) throw Exception("Server returned invalid packet")

            val length = inputStream.readVarInt()
            if (length == -1) throw Exception("Server prematurely ended stream")
            if (length == 0) throw Exception("Server returned unexpected value")

            val data = ByteArray(length)
            inputStream.readFully(data)
            val jsonData = String(data, options.charset)

            //> Ping
            outputStream.writeByte(0x09) // Size of packet
            outputStream.writeByte(PACKET_PING)
            outputStream.writeLong(System.currentTimeMillis())

            //< Ping
            id = inputStream.readVarInt()
            if (id == -1) throw Exception("Server prematurely ended stream")
            //if (id != PACKET_PING) throw Exception("Server returned invalid packet")

            val ping = measureTimeMillis {
                inputStream.readVarInt()
            }

            val result = json.decodeFromString<ServerPingResult>(jsonData)

            var favicon: ByteArray? = cachedImages["${options.hostname}:${options.port}"]
            if (favicon == null && result.favicon != null) {
                val trim = result.favicon
                    .replace("data:image/png;", "")
                    .replace("base64,", "")
                val bytes = Base64.decode(trim)
                cachedImages["${options.hostname}:${options.port}"] = bytes
                favicon = bytes
            }
            outputStream.flush()

            return ServerPong(
                ping = ping,
                max = result.players.max,
                online = result.players.online,
                serverStatus = ServerStatus.ONLINE,
                favicon = favicon
            )

        } catch (e: Exception) {
            println(e)
            cachedConnections.remove("${options.hostname}:${options.port}")
            return ServerPong(
                ping = -1,
                serverStatus = ServerStatus.OFFLINE
            )
        }
    }

    companion object {
        const val SRV_QUERY_PREFIX = "_minecraft._tcp.%s"
    }
}
