package ru.alterland.launcher.ui.screen.main.client.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.out_of
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.clientprofile.ClientStatus
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.util.extentions.ByteSuffix.Companion.getByteSuffix
import ru.alterland.launcher.util.extentions.ByteSuffix.Companion.toHumanReadable

@Composable
fun UpdateProgress(
    modifier: Modifier = Modifier,
    clientStatus: ClientStatus.Updating,
    onClick: () -> Unit
) {
    val progress = if (clientStatus.total == 0L) {
        0f
    } else {
        clientStatus.received.toFloat() / clientStatus.total.toFloat()
    }

    val received = remember(clientStatus.received) { clientStatus.received.getByteSuffix() }
    val total = remember(clientStatus.total) { clientStatus.total.getByteSuffix() }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${received.toHumanReadable(clientStatus.received)} ${stringResource(Res.string.out_of)} ${total.toHumanReadable(clientStatus.total)}",
                color = AppTheme.colors.labelPrimary,
                textAlign = TextAlign.End,
                fontSize = 12.sp
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                progress = progress,
                color = AppTheme.colors.primary,
                backgroundColor = AppTheme.colors.forceWhiteTertiary,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}
