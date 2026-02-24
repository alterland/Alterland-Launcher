package ru.alterland.launcher.domain.model

data class Store(
    val accessToken: String?,
    val refreshToken: String?,
    val rememberMe: Boolean?,
    val minecraftSettings: Map<String, MinecraftSettings>?
) {
    data class MinecraftSettings(
        val launchAfterUpdate: Boolean,
        val launchFullscreen: Boolean,
        val autoConnect: Boolean,
        val screenWidth: Int,
        val screenHeight: Int,
        val currentDir: String,
        val ram: Int
    )
}
