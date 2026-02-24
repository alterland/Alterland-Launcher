package ru.alterland.launcher.data.source.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreV1(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("rememberMe") val rememberMe: Boolean? = null,
    @SerialName("minecraftSettings") val minecraftSettings: Map<String, MinecraftSettings>? = null,
) {
    @Serializable
    data class MinecraftSettings(
        @SerialName("launchAfterUpdate") val launchAfterUpdate: Boolean? = null,
        @SerialName("launchFullscreen") val launchFullscreen: Boolean? = null,
        @SerialName("autoConnect") val autoConnect: Boolean? = null,
        @SerialName("screenWidth") val screenWidth: Int? = null,
        @SerialName("screenHeight") val screenHeight: Int? = null,
        @SerialName("currentDir") val currentDir: String? = null,
        @SerialName("ram") val ram: Int? = null
    )
}
