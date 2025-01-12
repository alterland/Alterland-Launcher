package ru.alterland.launcher.data.source.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ServerProfileRequest(
    val title: String?,
    val description: String?,
    val ip: String?,
    val port: Int?,
    val clientProfile: String?
)