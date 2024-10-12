package ru.alterland.launchercore.domain.model.externalindex

data class AssetsExternalIndexType(
    val objects: Map<String, Item>
): ExternalIndex {

    data class Item(
        val hash: String,
        val size: Long
    )
}
