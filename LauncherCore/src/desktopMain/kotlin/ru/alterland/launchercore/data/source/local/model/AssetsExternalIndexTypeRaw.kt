package ru.alterland.launchercore.data.source.local.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetsExternalIndexesTypeRaw(
    val objects: Map<String, Item>?
) {

    @Serializable
    data class Item(
        val hash: String?,
        val size: Long?
    )
}
