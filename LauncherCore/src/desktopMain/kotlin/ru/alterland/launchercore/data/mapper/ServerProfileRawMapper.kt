package ru.alterland.launchercore.data.mapper

import ru.alterland.launchercore.data.source.local.model.ServerAddressRaw
import ru.alterland.launchercore.data.source.local.model.ServerProfileRaw
import ru.alterland.launchercore.domain.model.ServerAddress
import ru.alterland.launchercore.domain.model.ServerProfile

fun ServerProfileRaw.toDomain() = ServerProfile(
    sortIndex = sortIndex ?: -1,
    id = id.orEmpty(),
    name = name.orEmpty(),
    title = title.orEmpty(),
    titleUrl = titleUrl.orEmpty(),
    titleLocalPath = titleLocalPath.orEmpty(),
    description = description.orEmpty(),
    backgroundUrl = backgroundUrl.orEmpty(),
    backgroundLocalPath = backgroundLocalPath.orEmpty(),
    address = address?.toDomain(),
    clientProfile = clientProfile
)

fun ServerAddressRaw.toDomain() = ServerAddress(
    host = ip.orEmpty(),
    port = port ?: 0
)
