package ru.alterland.launcher.domain.model

data class Store(
    val accessToken: String?,
    val rememberMe: Boolean?,
    val launchAfterUpdate: Boolean?,
    val launchFullscreen: Boolean?,
    val screenWidth: Int?,
    val screenHeight: Int?,
    val currentDir: String?,
    val clientSettings: Map<String, ClientSettings>?
) {
    data class ClientSettings(
        val ram: Int
    )
}
