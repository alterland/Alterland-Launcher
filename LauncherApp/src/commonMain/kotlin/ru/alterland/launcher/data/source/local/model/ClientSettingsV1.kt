package ru.alterland.launcher.data.source.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientSettingsV1(
    @SerialName("ram") val ram: Int? = 4096
)
