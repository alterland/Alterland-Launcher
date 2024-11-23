package ru.alterland.launcher.ui.screen.main.serverinfo

import alterlandlauncher.composeapp.generated.resources.Res
import alterlandlauncher.composeapp.generated.resources.ic_settings
import alterlandlauncher.composeapp.generated.resources.role_play_bg
import alterlandlauncher.composeapp.generated.resources.rp_logo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launchercore.domain.model.ClientStatus

@Composable
fun ServerInfo(
    state: ServerInfoContract.State,
    setEvent: (e: ServerInfoContract.Event) -> Unit,
    navigateToClientSettings: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.serversCount })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            setEvent(ServerInfoContract.Event.OnServerSelected(page))
        }
    }

    if (state.currentServerProfile == null) return

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
            Image(
                painter = painterResource(Res.drawable.role_play_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
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
                Box(
                    modifier = Modifier
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
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.rp_logo),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                Text(
                    state.currentServerProfile.description,
                    color = AppTheme.colors.forceWhitePrimary,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                when {
                    state.isFetchingClientProfile -> PlayButton(clientStatus = ClientStatus.Verification)
                    state.currentClientProfile != null -> {
                        Row {
                            PlayButton(clientStatus = state.currentClientProfile.status) {
                                setEvent(ServerInfoContract.Event.OnPlayClicked(state.currentClientProfile))
                            }
                            Button(
                                icon = painterResource(Res.drawable.ic_settings),
                                backgroundColor = AppTheme.colors.backgroundElevatedTertiary,
                                modifier = Modifier.padding(start = 6.dp).size(34.dp),
                                onClick = navigateToClientSettings
                            )
                        }
                    }
                }
            }
        }
    }
}
