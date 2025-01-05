package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.composeapp.generated.resources.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.theme.defaultElementsShape
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MiniServer(
    item: MiniServerItem,
    modifier: Modifier = Modifier,
) {

//    val bitmap = if (item.serverStatus is MinecraftServerStatus.Online) {
//        item.serverStatus.favicon?.let {
//            val imageByteArray = Base64.decode(it.replace("data:image/png;base64,", ""))
//            Image.Companion.makeFromEncoded(imageByteArray).toComposeImageBitmap()
//        }
//    } else {
//        null
//    }
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
            if (null != null) {
//                Image(
//                    bitmap = null,
//                    contentScale = ContentScale.Crop,
//                    contentDescription = null,
//                    modifier = Modifier.size(32.dp).clip(defaultElementsShape)
//                )
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
        ServerStatus(item.serverStatus)
    }
}
