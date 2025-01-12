package ru.alterland.launcher.domain.model

data class ServerProfile(
    val id: String,
    val title: String,
    val description: String,
    val ip: String,
    val port: Int,
    val clientProfile: String?
)
