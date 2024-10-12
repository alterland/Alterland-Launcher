package ru.alterland.launchercore.data.mapper

import AlterlandLauncher.LauncherCore.BuildConfig
import ru.alterland.launchercore.data.source.local.model.AssetsExternalIndexesTypeRaw
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.externalindex.AssetsExternalIndexType
import java.nio.file.Path

fun AssetsExternalIndexesTypeRaw.toDomain(basePath: Path): List<ClientProfile.DownloadIndex> =
    objects?.mapNotNull { it.value.getItem() }?.mapNotNull { it.getDownloadIndex(basePath) } ?: listOf()

private fun AssetsExternalIndexesTypeRaw.Item.getItem(): AssetsExternalIndexType.Item? =
    if (hash != null && size != null) {
        AssetsExternalIndexType.Item(hash = hash, size = size)
    } else {
        null
    }

private fun AssetsExternalIndexType.Item.getDownloadIndex(basePath: Path): ClientProfile.DownloadIndex? =
    if (hash.length > 2) {
        val firstTwo = hash.substring(0, 2)
        ClientProfile.DownloadIndex(
            path = basePath.resolve("$firstTwo/$hash").toString(),
            checkSum = hash,
            size = size,
            url = "${BuildConfig.MOJANG_ASSETS_HOST}/$firstTwo/$hash",
            classPath = false,
            rules = listOf()
        )
    } else {
        null
    }
