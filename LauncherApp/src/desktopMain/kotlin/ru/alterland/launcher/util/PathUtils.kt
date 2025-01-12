package ru.alterland.launcher.util

import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists

fun Path.deleteFileAndCreateEmpty() {
    deleteIfExists()
    createParentDirectories()
    createFile()
}
