package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.theme.defaultElementsShape
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MiniServer(
    item: MiniServerItem,
    modifier: Modifier = Modifier,
) {

    val bitmap = if (item.serverStatus is MinecraftServerStatus.Online) {
        item.serverStatus.favicon?.let {
            val imageByteArray = Base64.decode(it.replace("data:image/png;base64,", ""))
            imageByteArray.decodeToImageBitmap()
        }
    } else {
        null
    }

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
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    modifier = Modifier.padding(start = 16.dp, end = 4.dp),
                    text = item.name,
                    color = AppTheme.colors.labelPrimary,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                when(item.serverStatus) {
                    is MinecraftServerStatus.Online -> {
                        Text(
                            text = stringResource(Res.string.server_status_players, item.serverStatus.onlinePlayers, item.serverStatus.maxPlayers),
                            color = AppTheme.colors.labelPrimary,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Text(
                            text = stringResource(
                                Res.string.server_status_latency,
                                item.serverStatus.latency
                            ),
                            color = AppTheme.colors.labelPrimary,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    MinecraftServerStatus.Polling -> {
                        Text(
                            stringResource(Res.string.server_status_polling),
                            color = AppTheme.colors.labelPrimary,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    MinecraftServerStatus.Offline -> {
                        Text(
                            stringResource(Res.string.server_status_offline),
                            color = AppTheme.colors.labelPrimary,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
        ServerStatus(status = item.serverStatus)
    }
}

@Preview
@Composable
private fun Preview() = AppTheme {
    MiniServer(
        item = MiniServerItem(id = "", name = "test", serverStatus = MinecraftServerStatus.Offline)
    )
}
