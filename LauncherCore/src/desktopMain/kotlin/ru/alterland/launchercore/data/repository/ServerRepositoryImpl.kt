package ru.alterland.launchercore.data.repository

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import ru.alterland.launchercore.data.mapper.toDomain
import ru.alterland.launchercore.data.source.minecraft.MinecraftClient
import ru.alterland.launchercore.domain.model.MinecraftServerStatus
import ru.alterland.launchercore.domain.repository.ServerRepository

class ServerRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val json: Json
): ServerRepository {

    private val ktorSelectorManager by lazy { SelectorManager(dispatcherIo) }

    override suspend fun ping(host: String, port: Int): MinecraftServerStatus {
        val client = connect(host, if (port <= 0) DEFAULT_PORT else port)
        val statusResponse = client.requestStatus(4)
        client.close()
        return statusResponse.toDomain()
    }

    private suspend fun connect(host: String, port: Int): MinecraftClient {
        val socketFactory = aSocket(ktorSelectorManager).configure {
            reuseAddress = true
            reusePort = true
            typeOfService = TypeOfService.IPTOS_LOWDELAY
            if (this is SocketOptions.TCPClientSocketOptions) {
                keepAlive = false
                noDelay = true
                lingerSeconds = 10
                socketTimeout = 10
            }
        }
        val socket = socketFactory.tcp().connect(host, port)
        return MinecraftClient(dispatcherIo = dispatcherIo, json = json, connection = socket)
    }

    companion object {
        const val DEFAULT_PORT = 25565
    }
}
