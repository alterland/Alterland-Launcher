package ru.alterland.launchercore.data.repository

import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.xbill.DNS.*
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


class ServerRepositoryImpl(
    private val json: Json
): ServerRepository {

    private val cachedImages = mutableMapOf<String, ByteArray>()

    override fun ping(hostname: String, port: Int?): ServerPong =
        ping(PingOptions(hostname = hostname, port = port ?: DEFAULT_PORT))

    @OptIn(ExperimentalEncodingApi::class)
    override fun ping(options: PingOptions): ServerPong {
        var hostname = options.hostname
        var port = options.port

        val records = Lookup(String.format(SRV_QUERY_PREFIX, options.hostname), Type.SRV).run()

        if (records != null) {
            for (record in records) {
                val srv = record as SRVRecord
                hostname = srv.target.toString().replaceFirst("\\.$", "")
                port = srv.port
            }
        }

        var ping: Long = -1

        val socket = Socket()

        try {
            val start = System.currentTimeMillis()
            socket.connect(InetSocketAddress(hostname, port), options.timeout)
            ping = System.currentTimeMillis() - start

            val inputStream = DataInputStream(socket.getInputStream())
            val outputStream = DataOutputStream(socket.getOutputStream())

            //> Handshake
            val handshakeBytes = ByteArrayOutputStream()
            val handshake = DataOutputStream(handshakeBytes)

            handshake.writeByte(PACKET_HANDSHAKE)
            handshake.writeVarInt(options.protocolVersion)
            handshake.writeVarInt(options.hostname.length)
            handshake.writeBytes(options.hostname)
            handshake.writeShort(options.port)
            handshake.writeVarInt(STATUS_HANDSHAKE)

            outputStream.writeVarInt(handshakeBytes.size())
            outputStream.write(handshakeBytes.toByteArray())

            //> Status request
            outputStream.writeByte(0x01) // Size of packet
            outputStream.writeByte(PACKET_STATUS_REQUEST)

            //< Status response
            inputStream.readVarInt() // Size

            var id = inputStream.readVarInt()

            if (id == -1) throw Exception("Server prematurely ended stream")
            if (id != PACKET_STATUS_REQUEST) throw Exception("Server returned invalid packet")

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
//            if (id != PACKET_PING) throw Exception("Server returned invalid packet")

            val result = json.decodeFromString<ServerPingResult>(jsonData)

            var favicon: ByteArray? = cachedImages["$hostname:$port"]
            if (favicon == null && result.favicon != null) {
                val trim = result.favicon
                    .replace("data:image/png;", "")
                    .replace("base64,", "")
                val bytes = Base64.decode(trim)
                cachedImages["$hostname:$port"] = bytes
                favicon = bytes
            }

            return ServerPong(
                ping = ping,
                max = result.players.max,
                online = result.players.online,
                serverStatus = ServerStatus.ONLINE,
                favicon = favicon
            )

        } catch (e: Exception) {
            return ServerPong(
                ping = ping,
                serverStatus = ServerStatus.OFFLINE
            )
        }
    }

    companion object {
        const val SRV_QUERY_PREFIX = "_minecraft._tcp.%s"
    }
}
