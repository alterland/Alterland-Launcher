package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.local.model.StoreV1
import ru.alterland.launcher.domain.model.Store

fun StoreV1.toDomain() = Store(
    accessToken = accessToken,
    refreshToken = refreshToken,
    rememberMe = rememberMe,
    minecraftSettings = minecraftSettings?.mapValues { it.value.toDomain() }
)

expect fun StoreV1.MinecraftSettings.toDomain(): Store.MinecraftSettings

fun Store.toVersion() = StoreV1(
    accessToken = accessToken,
    refreshToken = refreshToken,
    rememberMe = rememberMe,
    minecraftSettings = minecraftSettings?.mapValues { it.value.toVersion() }
)

expect fun Store.MinecraftSettings.toVersion(): StoreV1.MinecraftSettings
