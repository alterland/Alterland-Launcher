package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.network.model.dto.ModelTypeDto
import ru.alterland.launcher.data.source.network.model.response.SkinResponse
import ru.alterland.launcher.domain.model.Skin

fun SkinResponse.toDomain(): Skin = Skin(
    id = id.orEmpty(),
    name = name.orEmpty(),
    url = imageUrl.orEmpty(),
    modelType = modelType.toDomain()
)

fun Skin.ModelType.toDto(): ModelTypeDto = when (this) {
    Skin.ModelType.SLIM -> ModelTypeDto.SLIM
    else -> ModelTypeDto.WIDE
}

private fun ModelTypeDto?.toDomain(): Skin.ModelType = when (this) {
    ModelTypeDto.SLIM -> Skin.ModelType.SLIM
    else -> Skin.ModelType.WIDE
}
