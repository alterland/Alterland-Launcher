package ru.alterland.launcher

import android.content.Context

actual class PlatformConfiguration constructor(private val androidContext: Context) {
    actual val defaultDir: String = androidContext.filesDir.path
}
