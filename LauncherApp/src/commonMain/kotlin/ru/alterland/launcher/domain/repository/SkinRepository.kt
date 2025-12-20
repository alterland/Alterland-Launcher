package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.Skin

interface SkinRepository {
    suspend fun loadSkin(serverId: String): Skin
    suspend fun loadSkins(serverId: String): List<Skin>
    suspend fun saveSkin(serverId: String, skin: Skin)
}
