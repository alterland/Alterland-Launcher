package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    @SerialName("id") val id: String?,
    @SerialName("email") val email: String?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("realName") val realName: String?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("role") val role: String?,
    @SerialName("skin") val skin: SkinResponse?
)
