package ru.alterland.launcher

import ru.alterland.launcher.util.OS

actual class PlatformConfiguration constructor(val androidContext: Context) {
    actual val rootDir: String = androidContext.filesDir.path
    actual val storeDir: String = "${rootDir}/store.json"
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "Android"))
    actual val osVersion: String = "${android.os.Build.VERSION.SDK_INT}"
}
