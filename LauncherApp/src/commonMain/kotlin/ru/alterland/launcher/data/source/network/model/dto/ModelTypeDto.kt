package ru.alterland.launcher.data.source.network.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModelTypeDto {
    @SerialName("wide") WIDE,
    @SerialName("slim") SLIM
}