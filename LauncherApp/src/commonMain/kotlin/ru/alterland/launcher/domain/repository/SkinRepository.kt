package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.Skin

interface SkinRepository {
    fun getSkins(): List<Skin>
    suspend fun addSkin(path: String)
    suspend fun deleteSkin(skin: Skin)
    suspend fun renameSkin(skin: Skin, newName: String)
}