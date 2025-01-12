package ru.alterland.launcher.domain.model

sealed class MinecraftServerStatus {
    data class Online(
        val favicon: String?,
        val maxPlayers: Int,
        val onlinePlayers: Int,
        val version: String,
        val latency: Long
    ): MinecraftServerStatus()

    data object Offline: MinecraftServerStatus()
    data object Polling: MinecraftServerStatus()
}
