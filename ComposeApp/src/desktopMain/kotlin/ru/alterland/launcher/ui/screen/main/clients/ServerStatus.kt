package ru.alterland.launcher.ui.screen.main.clients

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import ru.alterland.launcher.Res
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launchercore.domain.model.ServerStatus

@Composable
fun ServerStatus(
    status: ServerStatus = ServerStatus.POLLING,
    modifier: Modifier = Modifier
) {
    val serverStatusCircle = painterResource(Res.image.ic_server_status)

    val statusColor = when(status) {
        ServerStatus.ONLINE -> AppTheme.colors.green
        ServerStatus.OFFLINE -> AppTheme.colors.red
        ServerStatus.POLLING -> AppTheme.colors.gray
    }

    Image(
        painter = serverStatusCircle,
        contentDescription = null,
        colorFilter = ColorFilter.tint(statusColor)
    )
}
