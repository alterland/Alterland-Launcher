package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClientProfileObjectResponse(
    val key: String?,
    val size: Long?,
    val lastModified: String?,
    val url: String?
)
