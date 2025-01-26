package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.source.local.model.ClientSettingsV1
import ru.alterland.launcher.data.source.local.model.StoreV1
import ru.alterland.launcher.domain.model.Store

fun StoreV1.toDomain() = Store(
    accessToken = accessToken.orEmpty(),
    rememberMe = rememberMe == true,
    launchAfterUpdate = launchAfterUpdate == true,
    launchFullscreen = launchFullscreen == true,
    screenWidth = screenWidth ?: 0,
    screenHeight = screenHeight ?: 0,
    clientSettings = clientSettings?.mapValues { entry -> entry.value.toDomain() }.orEmpty()
)

fun ClientSettingsV1.toDomain() = Store.ClientSettings(
    ram = ram ?: 0
)

fun Store.toVersion() = StoreV1(
    accessToken = accessToken,
    rememberMe = rememberMe,
    launchAfterUpdate = launchAfterUpdate,
    launchFullscreen = launchFullscreen,
    screenWidth = screenWidth,
    screenHeight = screenHeight,
    clientSettings = clientSettings.mapValues { entry -> entry.value.toVersion() }
)

fun Store.ClientSettings.toVersion() = ClientSettingsV1(
    ram = ram
)
