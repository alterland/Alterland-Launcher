package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServerProfileResponse(
    val id: String?,
    val title: String?,
    val description: String?,
    val ip: String?,
    val port: Int?,
    val clientProfile: String?
)
