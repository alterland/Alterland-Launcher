package ru.alterland.launcher.ui.screen.main.clients

import alterlandlauncher.composeapp.generated.resources.Res
import alterlandlauncher.composeapp.generated.resources.client_cover
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.Image
import ru.alterland.launcher.ui.screen.main.container.MenuItem
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.theme.defaultElementsShape

@Composable
fun MenuClientItem(
    item: MenuItem,
    modifier: Modifier = Modifier,
) {

    val bitmap = item.favicon?.let { Image.Companion.makeFromEncoded(it).toComposeImageBitmap() }
    val defaultImage = painterResource(Res.drawable.client_cover)

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .background(AppTheme.colors.backgroundElevatedSecondary, RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(defaultElementsShape)
                )
            } else {
                Image(
                    painter = defaultImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(defaultElementsShape)
                )
            }
            Column {
                Text(
                    item.name,
                    color = AppTheme.colors.labelPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                if (item.serverStatus == ru.alterland.launchercore.domain.model.ServerStatus.ONLINE) {
                    Text(
                        "Игроков: ${item.online} / ${item.max}",
                        color = AppTheme.colors.labelPrimary,
                        fontSize = 8.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text(
                        "Пинг: ${item.ping} мс",
                        color = AppTheme.colors.labelPrimary,
                        fontSize = 8.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
        ServerStatus(item.serverStatus)
    }
}
