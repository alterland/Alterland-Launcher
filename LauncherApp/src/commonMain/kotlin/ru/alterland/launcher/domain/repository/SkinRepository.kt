package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.Skin

interface SkinRepository {
    suspend fun getLibrarySkins(): List<Skin>
    suspend fun setUserSkin(skinId: String): Skin
    suspend fun uploadCustomSkin(filePath: String, modelType: Skin.ModelType)
}
