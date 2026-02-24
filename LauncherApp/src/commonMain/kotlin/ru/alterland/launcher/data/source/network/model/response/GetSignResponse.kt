package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSignResponse (

    @SerialName("user") val user: GetUserResponse,
    @SerialName("tokens") val tokens: GetTokensResponse
)