package ru.alterland.launcher.util.extentions

import alterlandlauncher.launcherapp.generated.resources.*
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

suspend fun Long.humanReadableByteCount() = when {
    this == Long.MIN_VALUE || this < 0 -> "N/A"
    this < 1024L -> getPluralString(Res.plurals.bytes, this.toInt())
    this <= 0xfffccccccccccccL shr 40 -> "%.1f ${getString(Res.string.kilobytes)}".format(this.toDouble() / (0x1 shl 10))
    this <= 0xfffccccccccccccL shr 30 -> "%.1f ${getString(Res.string.megabytes)}".format(this.toDouble() / (0x1 shl 20))
    this <= 0xfffccccccccccccL shr 20 -> "%.1f ${getString(Res.string.gigabytes)}".format(this.toDouble() / (0x1 shl 30))
    this <= 0xfffccccccccccccL shr 10 -> "%.1f ${getString(Res.string.terabytes)}".format(this.toDouble() / (0x1 shl 40))
    this <= 0xfffccccccccccccL -> "%.1f ${getString(Res.string.petabytes)}".format((this shr 10).toDouble() / (0x1 shl 40))
    else -> "%.1f ${getString(Res.string.exabytes)}".format((this shr 20).toDouble() / (0x1 shl 40))
}
