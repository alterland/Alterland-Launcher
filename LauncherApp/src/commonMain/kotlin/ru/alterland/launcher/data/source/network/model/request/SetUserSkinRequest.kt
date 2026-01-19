package ru.alterland.launcher.data.source.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetUserSkinRequest(
    @SerialName("skinId") val skinId: String
)
