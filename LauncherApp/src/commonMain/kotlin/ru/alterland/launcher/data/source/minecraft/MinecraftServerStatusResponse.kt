package ru.alterland.launcher.data.source.minecraft

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
        @SerialName("max") val max: Int?,
        @SerialName("online") val online: Int?
    )

    @Serializable
    data class Version(
        @SerialName("name")val name: String?,
        @SerialName("protocol")val protocol: Int?
    )
}
