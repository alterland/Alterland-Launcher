package ru.alterland.launchercore.data.source.minecraft

//credits: https://github.com/layou233

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.Closeable
import kotlin.system.measureTimeMillis

private const val NEXT_STATE_STATUS: Byte = 1
private const val NEXT_STATE_LOGIN: Byte = 2

private const val ID_HANDSHAKE: Byte = 0
private const val ID_STATUS: Byte = 0
private const val ID_PING: Byte = 1

class MinecraftClient(
    private val dispatcherIo: CoroutineDispatcher,
    private val json: Json,
    private val connection: Socket
) : Closeable {

    private val readChannel = connection.openReadChannel()
    private val writeChannel = connection.openWriteChannel()

    suspend fun requestStatus(version: Int): MinecraftServerStatusResponse {
        // https://wiki.vg/Protocol#Status_Request
        sendHandshake(version, NEXT_STATE_STATUS, byteArrayOf(1, ID_STATUS))

        readChannel.readVarInt() // packet length, ignored
        val packetID = readChannel.readByte()
        if (packetID != ID_STATUS) {
            throw UnexpectedPacketException(ID_STATUS, packetID)
        }
        val statusLength = readChannel.readVarInt()
        val statusArray = ByteArray(statusLength)
        readChannel.readFully(statusArray)
        val serverStatusResponse = statusArray.toString(Charsets.UTF_8)
        val serverStatus = json.decodeFromString<MinecraftServerStatusResponse>(serverStatusResponse)

        // https://wiki.vg/Server_List_Ping#Ping_Request
        writeChannel.write(10) { buffer ->
            buffer.put(9)
            buffer.put(ID_PING)
            buffer.putLong(System.currentTimeMillis())
        }
        writeChannel.flush()
        serverStatus.latency = measureTimeMillis {
            readChannel.awaitContent()
        }
        return serverStatus
    }

    private suspend fun sendHandshake(version: Int, nextState: Byte, appendix: ByteArray?) {
        val address = connection.remoteAddress as InetSocketAddress
        val hostname = address.hostname.toByteArray()
        val estimatedLength =
            1 + estimateVarIntBinaryLength(version) + estimateVarIntBinaryLength(hostname.size) + hostname.size + 2 + 1
        writeChannel.write(
            estimatedLength + estimateVarIntBinaryLength(estimatedLength) + (appendix?.size
                ?: 0)
        ) { buffer ->
            buffer.putVarInt(estimatedLength)
            buffer.put(ID_HANDSHAKE)
            buffer.putVarInt(version)
            buffer.putVarInt(hostname.size)
            buffer.put(hostname)
            buffer.putShort(address.port.toShort())
            buffer.put(nextState)
            appendix?.let { buffer.put(appendix) }
        }
        writeChannel.flush()
    }

    override fun close() {
        runBlocking(dispatcherIo) { writeChannel.flush() }
        connection.close()
    }

    class UnexpectedPacketException(wanted: Byte, got: Byte) : Exception("wanted $wanted, got $got")

}
