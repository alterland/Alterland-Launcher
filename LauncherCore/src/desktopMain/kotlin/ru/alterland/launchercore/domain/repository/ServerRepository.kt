package ru.alterland.launchercore.domain.repository

import ru.alterland.launchercore.domain.model.MinecraftServerStatus

interface ServerRepository {
    suspend fun ping(host: String, port: Int): MinecraftServerStatus
}
