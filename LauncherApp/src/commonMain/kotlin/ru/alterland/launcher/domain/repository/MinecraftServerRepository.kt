package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.MinecraftServerStatus

interface MinecraftServerRepository {
    suspend fun ping(host: String, port: Int): MinecraftServerStatus
}
