package ru.alterland.launcher.data.source.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.alterland.launcher.data.source.network.model.dto.ModelTypeDto

@Serializable
data class UploadCustomSkinRequest(
    @SerialName("modelType") val modelType: ModelTypeDto
)
