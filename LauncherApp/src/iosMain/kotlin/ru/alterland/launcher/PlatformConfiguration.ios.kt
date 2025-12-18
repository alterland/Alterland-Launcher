package ru.alterland.launcher

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path

actual class PlatformConfiguration {
    actual val defaultDir: String = FileKit.filesDir.path
}
