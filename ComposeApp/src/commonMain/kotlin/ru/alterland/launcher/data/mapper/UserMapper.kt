package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.network.model.response.GetUserResponse
import ru.alterland.launcher.domain.entity.User

fun GetUserResponse.toDomain() = User(
    id = id.orEmpty(),
    accessToken = accessToken.orEmpty(),
    email = email.orEmpty(),
    nickname = nickname.orEmpty(),
    realName = realName.orEmpty()
)
