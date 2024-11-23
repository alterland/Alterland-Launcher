package ru.alterland.launchercore.domain.model

data class ServerProfile(
    val sortIndex: Int,
    val id: String,
    val name: String,
    val title: String,
    val titleUrl: String,
    val titleLocalPath: String,
    val description: String,
    val backgroundUrl: String,
    val backgroundLocalPath: String,
    val address: ServerAddress?,
    val clientProfile: String?,
    val serverStatus: MinecraftServerStatus = MinecraftServerStatus.Polling
)
