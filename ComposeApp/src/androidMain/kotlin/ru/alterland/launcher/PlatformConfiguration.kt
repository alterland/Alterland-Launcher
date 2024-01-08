package ru.alterland.launcher

import ru.alterland.launcher.util.OS

actual class PlatformConfiguration {
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "Android"))
    actual val osVersion: String = "${android.os.Build.VERSION.SDK_INT}"
}
