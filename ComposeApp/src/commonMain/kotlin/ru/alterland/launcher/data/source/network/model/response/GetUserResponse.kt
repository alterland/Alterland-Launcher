package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    @SerialName("id") val id: String?,
    @SerialName("accessToken") val accessToken: String?,
    @SerialName("email") val email: String?,
    @SerialName("nickname") val nickname: String?,
    @SerialName("realName") val realName: String?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("role") val role: Role?
) {
    @Serializable
    data class Role(
        @SerialName("id") val id: String?,
        @SerialName("name") val name: String?,
        @SerialName("strength") val strength: Int?
    )
}
