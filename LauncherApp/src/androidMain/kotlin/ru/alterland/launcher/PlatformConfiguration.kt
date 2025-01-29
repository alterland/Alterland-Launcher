package ru.alterland.launcher

import android.content.Context
import ru.alterland.launcher.util.OS

actual class PlatformConfiguration constructor(private val androidContext: Context) {
    actual val defaultDir: String = androidContext.filesDir.path
    actual val os: OS = OS.fromValue(System.getProperty("os.name", "Android"))
}
