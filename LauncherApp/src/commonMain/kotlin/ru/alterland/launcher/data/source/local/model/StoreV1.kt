package ru.alterland.launcher.data.source.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreV1(
    @SerialName("accessToken") val accessToken: String? = "",
    @SerialName("rememberMe") val rememberMe: Boolean? = true,
    @SerialName("launchAfterUpdate") val launchAfterUpdate: Boolean? = true,
    @SerialName("launchFullscreen") val launchFullscreen: Boolean? = false,
    @SerialName("screenWidth") val screenWidth: Int? = 600,
    @SerialName("screenHeight") val screenHeight: Int? = 400,
    @SerialName("clientSettings") val clientSettings: Map<String, ClientSettingsV1>? = mapOf()
)
