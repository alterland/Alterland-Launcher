package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTokensResponse(
    @SerialName("accessToken") val accessToken: String?,
    @SerialName("refreshToken") val refreshToken: String?
)