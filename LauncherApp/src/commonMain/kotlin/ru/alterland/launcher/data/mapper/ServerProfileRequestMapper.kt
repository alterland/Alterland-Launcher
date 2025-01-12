package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.network.model.request.ServerProfileRequest
import ru.alterland.launcher.domain.model.ServerProfile

fun ServerProfile.toRequest() = ServerProfileRequest(
    title = title,
    description = description,
    ip = ip,
    port = port,
    clientProfile = clientProfile
)
