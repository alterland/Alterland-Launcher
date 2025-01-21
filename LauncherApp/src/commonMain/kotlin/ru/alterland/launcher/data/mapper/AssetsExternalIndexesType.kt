package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.BuildConfig
import ru.alterland.launcher.data.source.network.model.response.AssetsExternalIndexesTypeResponse
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.externalindex.AssetsExternalIndexType
import ru.alterland.launcher.util.extentions.v

fun AssetsExternalIndexesTypeResponse.toDomain(basePath: String): List<ClientProfile.DownloadIndex> =
    objects?.mapNotNull { it.value.getItem() }?.mapNotNull { it.getDownloadIndex(basePath) } ?: listOf()

private fun AssetsExternalIndexesTypeResponse.Item.getItem(): AssetsExternalIndexType.Item? =
    if (hash != null && size != null) {
        AssetsExternalIndexType.Item(hash = hash, size = size)
    } else {
        null
    }

private fun AssetsExternalIndexType.Item.getDownloadIndex(basePath: String): ClientProfile.DownloadIndex? =
    if (hash.length > 2) {
        val firstTwo = hash.substring(0, 2)
        ClientProfile.DownloadIndex(
            path = basePath v firstTwo v hash,
            checkSum = hash,
            size = size,
            url = "${BuildConfig.MOJANG_ASSETS_HOST}/$firstTwo/$hash",
            classPath = false,
            allowChanges = false,
            rules = listOf()
        )
    } else {
        null
    }
