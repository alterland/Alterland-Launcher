package ru.alterland.launcher.data.repository

import ru.alterland.launcher.domain.model.Store
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.MinecraftSettingsRepository

class MinecraftSettingsRepositoryImpl(
    private val localStorage: LocalStorage
): MinecraftSettingsRepository {

    override suspend fun getSettings(id: String?): Store.MinecraftSettings =
        localStorage.get()?.minecraftSettings?.get(id) ?: Store.MinecraftSettings(
            launchAfterUpdate = LAUNCH_AFTER_UPDATE_DEFAULT,
            launchFullscreen = LAUNCH_FULL_SCREEN_DEFAULT,
            autoConnect = AUTO_CONNECT_DEFAULT,
            screenWidth = SCREEN_WIDTH_DEFAULT,
            screenHeight = SCREEN_HEIGHT_DEFAULT,
            currentDir = "",
            ram = 0
        )

    override suspend fun saveSettings(id: String, settings: Store.MinecraftSettings) =
        localStorage.update { store ->
            val minecraftSettings = store?.minecraftSettings.orEmpty().toMutableMap().apply {
                put(id, settings)
            }
            store?.copy(minecraftSettings = minecraftSettings)
        }

    companion object {
        const val LAUNCH_AFTER_UPDATE_DEFAULT = true
        const val LAUNCH_FULL_SCREEN_DEFAULT = false
        const val AUTO_CONNECT_DEFAULT = true
        const val SCREEN_WIDTH_DEFAULT = 600
        const val SCREEN_HEIGHT_DEFAULT = 400
    }
}
