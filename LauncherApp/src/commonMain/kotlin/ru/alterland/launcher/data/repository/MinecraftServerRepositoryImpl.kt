package ru.alterland.launcher.data.repository

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.minecraft.MinecraftClient
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.domain.repository.MinecraftServerRepository

class MinecraftServerRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val json: Json
): MinecraftServerRepository {

    private val ktorSelectorManager by lazy { SelectorManager(dispatcherIo) }

    override suspend fun ping(host: String, port: Int): MinecraftServerStatus = withContext(dispatcherIo) {
        val client = connect(host, if (port <= 0) DEFAULT_PORT else port)
        val statusResponse = client.requestStatus(4)
        client.close()
        statusResponse.toDomain()
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
