package ru.alterland.launcher

import android.content.Context
import ru.alterland.launcher.util.OS

actual class PlatformConfiguration constructor(private val androidContext: Context) {
    actual val rootDir: String = androidContext.filesDir.path
    actual val storeDir: String = rootDir
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "Android"))
    actual val osVersion: String = "${android.os.Build.VERSION.SDK_INT}"
}
