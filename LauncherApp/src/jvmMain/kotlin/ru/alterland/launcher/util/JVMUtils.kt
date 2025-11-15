package ru.alterland.launcher.util

import com.sun.management.OperatingSystemMXBean
import ru.alterland.launcher.domain.model.clientprofile.OsArch
import ru.alterland.launcher.domain.model.clientprofile.OsName
import java.lang.management.ManagementFactory

private val OPERATING_SYSTEM_MXBEAN: OperatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean

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
