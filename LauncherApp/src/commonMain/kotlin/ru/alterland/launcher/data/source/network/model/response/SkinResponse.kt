package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.alterland.launcher.data.source.network.model.dto.ModelTypeDto

@Serializable
data class SkinResponse(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
    @SerialName("modelType") val modelType: ModelTypeDto?,
    @SerialName("imageUrl") val imageUrl: String?
)
