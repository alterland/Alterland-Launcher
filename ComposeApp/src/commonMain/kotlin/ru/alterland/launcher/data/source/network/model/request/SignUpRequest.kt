package ru.alterland.launcher.data.source.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("nickname") val nickname: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)