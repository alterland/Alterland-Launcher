package ru.alterland.launcher

import ru.alterland.launcher.util.OS

actual class PlatformConfiguration {
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "unknown"))
    actual val osVersion: String = System.getProperty("os.version", "")
}
