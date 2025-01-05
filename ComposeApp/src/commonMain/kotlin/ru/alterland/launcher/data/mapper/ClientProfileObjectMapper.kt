package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.network.model.response.ClientProfileObjectResponse
import ru.alterland.launcher.domain.model.ClientProfileObject

fun ClientProfileObjectResponse.toDomain() = ClientProfileObject(
    key = key.orEmpty(),
    size = size ?: 0,
    lastModified = lastModified.orEmpty(),
    url = url.orEmpty()
)
