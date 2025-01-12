package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_server_status
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun ServerStatus(
    status: MinecraftServerStatus
) {
    val serverStatusCircle = painterResource(Res.drawable.ic_server_status)

    val statusColor = when(status) {
        is MinecraftServerStatus.Online -> AppTheme.colors.green
        MinecraftServerStatus.Offline -> AppTheme.colors.red
        MinecraftServerStatus.Polling -> AppTheme.colors.gray
    }

    Image(
        painter = serverStatusCircle,
        contentDescription = null,
        colorFilter = ColorFilter.tint(statusColor)
    )
}
