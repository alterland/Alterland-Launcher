package ru.alterland.launchercore.util

import ru.alterland.launchercore.util.HashUtils.getCheckSumFromFile
import ru.alterland.launchercore.util.HashUtils.hashAlgorithm
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists

fun Path.getCheckSumFromFile() = getCheckSumFromFile(hashAlgorithm, 40)

fun Path.deleteFileAndCreateEmpty() {
    deleteIfExists()
    createParentDirectories()
    createFile()
}
