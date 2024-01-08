package ru.alterland.launchercore.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
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
    @Transient val pong: ServerPong = ServerPong()
)
