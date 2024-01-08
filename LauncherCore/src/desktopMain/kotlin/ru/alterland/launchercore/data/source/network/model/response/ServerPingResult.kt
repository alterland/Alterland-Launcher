package ru.alterland.launchercore.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerPingResult(
    @SerialName("players") val players: Players,
    @SerialName("version") val version: Version,
    @SerialName("favicon") val favicon: String?
) {

    @Serializable
    data class Players(
        @SerialName("max") val max: Int,
        @SerialName("online") val online: Int
    )

    @Serializable
    data class Version(
        @SerialName("name") val name: String,
        @SerialName("protocol") val protocol: Int
    )
}
