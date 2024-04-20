package ru.alterland.launchercore.data.source.network.model.response

import kotlinx.serialization.Serializable
import ru.alterland.launchercore.data.source.local.model.ClientProfileRaw

@Serializable
data class ExternalIndexResponse(
    val objects: List<ClientProfileRaw.Library>?
)
