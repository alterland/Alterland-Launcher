package ru.alterland.launcher.util.extentions

import kotlinx.io.files.Path
import kotlin.io.path.Path as JvmPath
import java.nio.file.FileSystems
import kotlin.io.path.exists
import kotlin.io.path.setPosixFilePermissions
import java.nio.file.attribute.PosixFilePermission

private val BIN_POSIX_PERMISSIONS = setOf(
    PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE,
    PosixFilePermission.GROUP_READ,  PosixFilePermission.GROUP_EXECUTE,
    PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_EXECUTE
)

actual fun Path.makeExecutable() {
    val path = JvmPath(this.toString())

    val isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")
    if (!isPosix) return

    if (!path.exists()) {
        throw IllegalStateException("Path does not exist: $path")
    }
    path.setPosixFilePermissions(BIN_POSIX_PERMISSIONS)
}
