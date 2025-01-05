package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.network.model.response.GetUserResponse
import ru.alterland.launcher.domain.model.User

fun GetUserResponse.toDomain() = User(
    id = id.orEmpty(),
    email = email.orEmpty(),
    nickname = nickname.orEmpty(),
    realName = realName.orEmpty(),
    role = role?.toDomain()
)

fun GetUserResponse.Role.toDomain() = User.Role(
    id = id.orEmpty(),
    name = name.orEmpty(),
    strength = strength ?: 0
)
