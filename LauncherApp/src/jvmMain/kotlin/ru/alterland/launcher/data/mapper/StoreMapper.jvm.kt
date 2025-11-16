package ru.alterland.launcher.data.mapper

import ru.alterland.launcher.data.repository.MinecraftSettingsRepositoryImpl.Companion.AUTO_CONNECT_DEFAULT
import ru.alterland.launcher.data.repository.MinecraftSettingsRepositoryImpl.Companion.LAUNCH_AFTER_UPDATE_DEFAULT
import ru.alterland.launcher.data.repository.MinecraftSettingsRepositoryImpl.Companion.LAUNCH_FULL_SCREEN_DEFAULT
import ru.alterland.launcher.data.repository.MinecraftSettingsRepositoryImpl.Companion.SCREEN_HEIGHT_DEFAULT
import ru.alterland.launcher.data.repository.MinecraftSettingsRepositoryImpl.Companion.SCREEN_WIDTH_DEFAULT
import ru.alterland.launcher.data.source.local.model.StoreV1
import ru.alterland.launcher.domain.model.Store

actual fun StoreV1.MinecraftSettings.toDomain(): Store.MinecraftSettings = Store.MinecraftSettings(
    launchAfterUpdate = launchAfterUpdate ?: LAUNCH_AFTER_UPDATE_DEFAULT,
    launchFullscreen = launchFullscreen ?: LAUNCH_FULL_SCREEN_DEFAULT,
    autoConnect = autoConnect ?: AUTO_CONNECT_DEFAULT,
    screenWidth = screenWidth ?: SCREEN_WIDTH_DEFAULT,
    screenHeight = screenHeight ?: SCREEN_HEIGHT_DEFAULT,
    currentDir = currentDir.orEmpty(),
    ram = ram ?: 0
)

actual fun Store.MinecraftSettings.toVersion(): StoreV1.MinecraftSettings = StoreV1.MinecraftSettings(
    launchAfterUpdate = launchAfterUpdate,
    launchFullscreen = launchFullscreen,
    autoConnect = autoConnect,
    screenWidth = screenWidth,
    screenHeight = screenHeight,
    currentDir = currentDir,
    ram = ram
)
