package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.Skin

interface SkinRepository {
    suspend fun loadSkin(): Skin
    suspend fun saveSkin(skin: Skin)
}
