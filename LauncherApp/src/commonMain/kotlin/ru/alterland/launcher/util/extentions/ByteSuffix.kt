package ru.alterland.launcher.util.extentions

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

enum class ByteSuffix {
    B, KB, MB, GB, TB, PB, EB, UNKNOWN;

    companion object {
        fun Long.getByteSuffix(): ByteSuffix = when {
            this == Long.MIN_VALUE || this < 0 -> UNKNOWN
            this < 1024L -> B
            this <= 0xfffccccccccccccL shr 40 -> KB
            this <= 0xfffccccccccccccL shr 30 -> MB
            this <= 0xfffccccccccccccL shr 20 -> GB
            this <= 0xfffccccccccccccL shr 10 -> TB
            this <= 0xfffccccccccccccL -> PB
            else -> EB
        }

        @Composable
        fun ByteSuffix.toHumanReadable(value: Long): String = when(this) {
            B -> pluralStringResource(Res.plurals.bytes, value.toInt())
            KB -> stringResource(Res.string.kilobytes)
            MB -> stringResource(Res.string.megabytes)
            GB -> stringResource(Res.string.gigabytes)
            TB -> stringResource(Res.string.terabytes)
            PB -> stringResource(Res.string.petabytes)
            EB -> stringResource(Res.string.exabytes)
            UNKNOWN -> "N/A"
        }
    }
}
