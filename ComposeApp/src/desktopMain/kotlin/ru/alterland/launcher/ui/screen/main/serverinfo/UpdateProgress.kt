package ru.alterland.launcher.ui.screen.main.serverinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.util.extentions.bytesToMegabytesString
import ru.alterland.launchercore.domain.model.ClientStatus

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

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${clientStatus.received.bytesToMegabytesString()} МБ из ${clientStatus.total.bytesToMegabytesString()} МБ",
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
