package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.add
import alterlandlauncher.launcherapp.generated.resources.just_empty
import alterlandlauncher.launcherapp.generated.resources.no_internet
import alterlandlauncher.launcherapp.generated.resources.retry
import alterlandlauncher.launcherapp.generated.resources.servers_empty_placeholder_title
import alterlandlauncher.launcherapp.generated.resources.unknown_exception
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.io.IOException
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button

@Composable
fun ServersPlaceholder(
    throwable: Throwable? = null,
    isEmpty: Boolean = false,
    canAddServer: Boolean = false,
    onAddServerClick: () -> Unit = {},
    onRetryClick: () -> Unit = {}
) {
    val descriptionText = when {
        throwable is IOException -> Res.string.no_internet
        isEmpty -> Res.string.just_empty
        else -> Res.string.unknown_exception
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.servers_empty_placeholder_title),
            color = AppTheme.colors.labelSecondary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(descriptionText),
            color = AppTheme.colors.labelTertiary,
            textAlign = TextAlign.Center
        )
        Button(
            text = stringResource(Res.string.retry),
            onClick = onRetryClick,
            backgroundColor = AppTheme.colors.backgroundElevatedTertiary,
            modifier = Modifier
                .padding(top = 14.dp)
                .width(155.dp)
                .height(35.dp)
        )
        if (canAddServer) {
            Button(
                text = stringResource(Res.string.add),
                onClick = onAddServerClick,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(155.dp)
                    .height(35.dp)
            )
        }
    }
}
