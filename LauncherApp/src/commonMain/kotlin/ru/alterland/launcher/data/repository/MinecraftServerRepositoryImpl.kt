package ru.alterland.launcher.data.repository

import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.domain.repository.MinecraftServerRepository
import tech.aliorpse.mcutils.api.MCServer
import tech.aliorpse.mcutils.api.getStatus

class MinecraftServerRepositoryImpl: MinecraftServerRepository {

    override suspend fun ping(host: String, port: Int): MinecraftServerStatus {
        val serverStatus = MCServer.getStatus(host = host, port = if (port <= 0) DEFAULT_PORT else port, enableSrv = false)
        return MinecraftServerStatus.Online(
            favicon = serverStatus.favicon,
            maxPlayers = serverStatus.players.max,
            onlinePlayers = serverStatus.players.online,
            version = serverStatus.version.name,
            latency = serverStatus.ping
        )
    }

    companion object {
        const val DEFAULT_PORT = 25565
    }
}
