package ru.alterland.launcher

import ru.alterland.launcher.util.CURRENT_DIRECTORY
import ru.alterland.launcher.util.OS

actual class PlatformConfiguration {
    actual val defaultDir: String = CURRENT_DIRECTORY
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "unknown"))
}
