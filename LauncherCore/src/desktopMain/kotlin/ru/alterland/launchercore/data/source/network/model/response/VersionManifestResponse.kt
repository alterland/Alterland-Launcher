package ru.alterland.launchercore.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionManifestResponse(
    @SerialName("latest") val latest: Latest,
    @SerialName("versions") val versions: List<VersionManifest>,
) {
    @Serializable
    data class Latest(
        @SerialName("release") val release: String,
        @SerialName("snapshot") val snapshot: String,
    )

    @Serializable
    data class VersionManifest(
        @SerialName("id") val id: String,
        @SerialName("type") val type: String,
        @SerialName("url") val url: String,
        @SerialName("time") val time: String,
        @SerialName("releaseTime") val releaseTime: String
    )
}
