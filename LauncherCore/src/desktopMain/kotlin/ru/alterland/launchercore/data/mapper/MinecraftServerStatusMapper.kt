package ru.alterland.launchercore.data.mapper

import ru.alterland.launchercore.data.source.minecraft.MinecraftServerStatusResponse
import ru.alterland.launchercore.domain.model.MinecraftServerStatus

fun MinecraftServerStatusResponse.toDomain() = MinecraftServerStatus.Online(
    favicon = favicon?.replace("data:image/png;base64,", ""),
    maxPlayers = players?.max ?: 0,
    onlinePlayers = players?.online ?: 0,
    version = version?.name.orEmpty(),
    latency = latency
)
