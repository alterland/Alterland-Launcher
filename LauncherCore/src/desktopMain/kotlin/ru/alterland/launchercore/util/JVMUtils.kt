package ru.alterland.launchercore.util

import com.sun.management.OperatingSystemMXBean
import ru.alterland.launchercore.domain.model.OsArch
import ru.alterland.launchercore.domain.model.OsName
import java.lang.management.ManagementFactory
import java.nio.file.FileSystems
import java.nio.file.attribute.PosixFilePermission

private val OPERATING_SYSTEM_MXBEAN: OperatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean

val USER_HOME: String = System.getProperty("user.home")
val OS_NAME: OsName = OsName.getOsType(System.getProperty("os.name"))
val OS_ARCH: OsArch = OsArch.getOsArchType(System.getProperty("os.arch"))
val OS_VERSION: String = System.getProperty("os.version")
val OS_BITS = if (OS_NAME == OsName.WINDOWS) {
    val pfx86 = System.getenv("ProgramFiles(x86)")
    if (pfx86.isNullOrEmpty()) 64 else 32
} else {
    if (System.getProperty("os.arch").contains("64")) 64 else 32
}
val DEVICE_RAM = OPERATING_SYSTEM_MXBEAN.totalMemorySize.shr(20).coerceAtMost(if (OS_BITS == 32) 1536 else 65534)

val IS_POSIX: Boolean = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")
val BIN_POSIX_PERMISSIONS: Set<PosixFilePermission> = setOf(
    PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE, // Owner
    PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_EXECUTE, // Group
    PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_EXECUTE // Others
)
