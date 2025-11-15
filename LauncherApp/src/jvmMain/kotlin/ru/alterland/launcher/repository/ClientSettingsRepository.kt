package ru.alterland.launcher.domain.repository

interface ClientSettingsRepository {
    fun getDefaultDirectory(): String
    suspend fun getCurrentDirectory(): String
    suspend fun setCurrentDirectory(dir: String)

    suspend fun getScreenWidth(): Int
    suspend fun getScreenHeight(): Int
}
