package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.Store

interface MinecraftSettingsRepository {
    suspend fun getSettings(id: String?): Store.MinecraftSettings
    suspend fun saveSettings(id: String, settings: Store.MinecraftSettings)
}
