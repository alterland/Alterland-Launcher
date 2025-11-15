package ru.alterland.launcher.data.repository

import kotlinx.io.files.FileSystem
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.domain.repository.ClientSettingsRepository
import ru.alterland.launcher.domain.repository.LocalStorage

class ClientSettingsRepositoryImpl(
    private val platformConfiguration: PlatformConfiguration,
    private val fileSystem: FileSystem,
    private val localStorage: LocalStorage
): ClientSettingsRepository {

    override fun getDefaultDirectory(): String = platformConfiguration.defaultDir

    override suspend fun getCurrentDirectory(): String = localStorage.get()?.currentDir ?: getDefaultDirectory()

    override suspend fun setCurrentDirectory(dir: String) {
        localStorage.update { it?.copy(currentDir = dir) }
    }

    override suspend fun getScreenWidth(): Int = localStorage.get()?.screenWidth ?: SCREEN_WIDTH_DEFAULT

    override suspend fun getScreenHeight(): Int = localStorage.get()?.screenWidth ?: SCREEN_HEIGHT_DEFAULT

    companion object {
        private const val SCREEN_WIDTH_DEFAULT = 600
        private const val SCREEN_HEIGHT_DEFAULT = 400
    }
}
