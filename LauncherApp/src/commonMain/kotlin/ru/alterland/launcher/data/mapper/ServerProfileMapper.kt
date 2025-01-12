package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.repository.MinecraftServerRepositoryImpl.Companion.DEFAULT_PORT
import ru.alterland.launcher.data.source.network.model.response.ServerProfileResponse
import ru.alterland.launcher.domain.model.ServerProfile

fun ServerProfileResponse.toDomain() = ServerProfile(
    id = id.orEmpty(),
    title = title.orEmpty(),
    description = description.orEmpty(),
    ip = ip.orEmpty(),
    port = port ?: DEFAULT_PORT,
    clientProfile = clientProfile
)
