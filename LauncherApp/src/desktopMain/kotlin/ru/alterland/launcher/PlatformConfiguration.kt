package ru.alterland.launcher

import net.harawata.appdirs.AppDirsFactory

actual class PlatformConfiguration {
    private val appDirs = AppDirsFactory.getInstance()
    private val userDataDir = appDirs.getUserDataDir(BuildConfig.WORK_FOLDER, null, null)

    actual val defaultDir: String = userDataDir
}
