package ru.alterland.launchercore.data.source.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AssetsIndexResponse(
    val objects: Map<String, AssetsIndexObject>?
) {

    @Serializable
    data class AssetsIndexObject(
        val hash: String?,
        val size: Long?
    )
}
