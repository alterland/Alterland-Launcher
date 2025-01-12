package ru.alterland.launcher.domain.model.clientprofile.externalindex

data class DefaultExternalIndexType(
    val objects: List<Item>
): ExternalIndex {

    data class Item(
        val path: String,
        val checkSum: String,
        val size: Long,
        val url: String
    )
}
