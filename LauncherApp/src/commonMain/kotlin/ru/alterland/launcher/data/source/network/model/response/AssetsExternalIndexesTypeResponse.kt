package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AssetsExternalIndexesTypeResponse(
    val objects: Map<String, Item>?
) {

    @Serializable
    data class Item(
        val hash: String?,
        val size: Long?
    )
}
