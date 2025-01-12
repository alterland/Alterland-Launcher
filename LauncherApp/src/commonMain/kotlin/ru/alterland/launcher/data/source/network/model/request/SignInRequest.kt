package ru.alterland.launcher.data.source.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String
)