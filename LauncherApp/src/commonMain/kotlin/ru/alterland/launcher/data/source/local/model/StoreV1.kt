package ru.alterland.launcher.data.source.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreV1(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("rememberMe") val rememberMe: Boolean? = null,
    @SerialName("launchAfterUpdate") val launchAfterUpdate: Boolean? = null,
    @SerialName("launchFullscreen") val launchFullscreen: Boolean? = null,
    @SerialName("screenWidth") val screenWidth: Int? = null,
    @SerialName("screenHeight") val screenHeight: Int? = null,
    @SerialName("currentDir") val currentDir: String? = null,
    @SerialName("clientSettings") val clientSettings: Map<String, ClientSettingsV1>? = null
)
