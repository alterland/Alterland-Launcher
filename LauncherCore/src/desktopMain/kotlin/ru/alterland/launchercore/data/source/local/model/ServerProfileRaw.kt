package ru.alterland.launchercore.data.source.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerProfileRaw(
    @SerialName("sortIndex") val sortIndex: Int?,
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
    @SerialName("title") val title: String?,
    @SerialName("titleUrl") val titleUrl: String?,
    @SerialName("titleLocalPath") val titleLocalPath: String?,
    @SerialName("description") val description: String?,
    @SerialName("backgroundUrl") val backgroundUrl: String?,
    @SerialName("backgroundLocalPath") val backgroundLocalPath: String?,
    @SerialName("ip") val ip: String?,
    @SerialName("port") val port: Int?,
    @SerialName("clientProfile") val clientProfile: String?
)
