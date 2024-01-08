package ru.alterland.launcher.ui.screen.main.serverinfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.Res
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launchercore.domain.model.ClientStatus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServerInfo(
    state: ServerInfoContract.State,
    setEvent: (e: ServerInfoContract.Event) -> Unit
) {
    val settingsIcon = painterResource(Res.image.ic_settings)

    val pagerState = rememberPagerState(pageCount = { state.servers.size })

    VerticalPager(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
        state = pagerState
    ) { page ->
        val server = state.servers[page]
        val client = state.clients.firstOrNull { it.id == server.clientProfile }

        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
            Image(
                painter = painterResource(Res.image.role_play_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0, 0, 0, 0x4D),
                                Color(0, 0, 0, 0x00),
                            )
                        )
                    )
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0, 0, 0, 0x00),
                                Color(0, 0, 0, 0x4D)
                            )
                        )
                    )
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.image.rp_logo),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                Text(
                    server.description,
                    color = AppTheme.colors.forceWhitePrimary,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                server.clientProfile?.let {
                    PlayButton(
                        modifier = Modifier.padding(bottom = 20.dp),
                        clientStatus = client?.status ?: ClientStatus.Unknown
                    ) {
                        setEvent(ServerInfoContract.Event.OnPlayClicked(server))
                    }
                }
            }
        }
    }
}
