package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckNicknameResponse(
    @SerialName("id") val id: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("createdAt") val createdAt: String,
)
