package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.network.model.response.GetUserResponse
import ru.alterland.launcher.domain.model.Role
import ru.alterland.launcher.domain.model.User

fun GetUserResponse.toDomain() = User(
    id = id.orEmpty(),
    email = email.orEmpty(),
    nickname = nickname.orEmpty(),
    realName = realName.orEmpty(),
    role = Role.fromValue(role),
    skin = skin?.toDomain()
)
