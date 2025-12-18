package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.local.model.StoreV1
import ru.alterland.launcher.domain.model.Store

actual fun StoreV1.MinecraftSettings.toDomain(): Store.MinecraftSettings =
    throw IllegalStateException("Invalid platform for Minecraft settings")

actual fun Store.MinecraftSettings.toVersion(): StoreV1.MinecraftSettings =
    throw IllegalStateException("Invalid platform for Minecraft settings")
