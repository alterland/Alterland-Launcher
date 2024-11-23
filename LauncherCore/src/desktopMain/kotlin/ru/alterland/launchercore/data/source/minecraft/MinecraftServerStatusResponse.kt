package ru.alterland.launchercore.data.source.minecraft

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class MinecraftServerStatusResponse(
    @SerialName("favicon") val favicon: String?,
    @SerialName("players") val players: Players?,
    @SerialName("version") val version: Version?,
    @Transient var latency: Long = -1,
) {
    @Serializable
    data class Players(
        val max: Int?,
        val online: Int?
    )

    @Serializable
    data class Version(
        val name: String?,
        val protocol: Int?
    )
}
