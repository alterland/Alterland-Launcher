package ru.alterland.launcher

import ru.alterland.launcher.util.OS
import ru.alterland.launcher.util.v

actual class PlatformConfiguration {
    actual val rootDir: String = System.getProperty("user.home") v BuildConfig.WORK_FOLDER
    actual val storeDir: String = rootDir v "store.json"
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "unknown"))
    actual val osVersion: String = System.getProperty("os.version", "")
}
