package ru.alterland.launcher.ui.screen.main.servers.client

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.clientprofile.ClientStatus
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.AppleCircularProgressIndicator
import ru.alterland.launcher.ui.widgets.Button

@Composable
fun PlayButton(
    modifier: Modifier = Modifier,
    clientStatus: ClientStatus,
    onClick: () -> Unit = {}
) {
    Row(modifier = modifier.height(34.dp)) {
        when(clientStatus) {
            ClientStatus.Ready, ClientStatus.Unknown -> {
                Button(
                    text = stringResource(Res.string.play),
                    backgroundColor = AppTheme.colors.primary,
                    modifier = Modifier.padding(start = 8.dp).width(153.dp),
                    onClick = onClick
                )
            }
            ClientStatus.Verification -> {
                AppleCircularProgressIndicator(
                    modifier = Modifier.size(34.dp),
                    color = Color(255,255,255,0x40),
                    strokeWidth = 3.dp
                )
            }
            ClientStatus.Launching -> {
                Button(
                    text = stringResource(Res.string.launching),
                    backgroundColor = AppTheme.colors.primary,
                    isEnabled = false,
                    modifier = Modifier.padding(start = 8.dp).width(153.dp),
                    onClick = onClick
                )
            }
            ClientStatus.Launched -> {
                Button(
                    text = stringResource(Res.string.launched),
                    backgroundColor = AppTheme.colors.green,
                    isEnabled = false,
                    modifier = Modifier.padding(start = 8.dp).width(153.dp),
                    onClick = onClick
                )
            }
            ClientStatus.UpdateRequired -> {
                Button(
                    text = stringResource(Res.string.update),
                    backgroundColor = Color(52, 120, 246),
                    modifier = Modifier.padding(start = 8.dp).width(153.dp),
                    onClick = onClick
                )
            }
            is ClientStatus.Updating -> UpdateProgress(clientStatus = clientStatus) { onClick() }
            is ClientStatus.UpdateError -> {
                Button(
                    text = stringResource(Res.string.retry),
                    backgroundColor = AppTheme.colors.primary,
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = onClick
                )
            }
        }
    }
}
