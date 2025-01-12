package ru.alterland.launcher.ui.screen.main.servers

import alterlandlauncher.launcherapp.generated.resources.*
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
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button

@Composable
fun Servers(
    state: ServersContract.State,
    onEvent: (e: ServersContract.Event) -> Unit,
    clientNavigation: @Composable () -> Unit,
    navigateToClientSettings: () -> Unit,
    navigateToEditServer: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.serversCount })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onEvent(ServersContract.Event.OnServerSelected(page))
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
                clientNavigation()
            }
            Button(
                icon = painterResource(Res.drawable.ic_apparel),
                backgroundColor = AppTheme.colors.backgroundElevatedTertiary,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .size(34.dp),
                onClick = navigateToClientSettings
            )
            if (state.userStrength >= User.Role.MIN_EDIT_STRENGTH) {
                Button(
                    icon = painterResource(Res.drawable.ic_edit),
                    backgroundColor = AppTheme.colors.backgroundElevatedTertiary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                        .size(34.dp),
                    onClick = { navigateToEditServer(state.currentServerProfile.id) }
                )
            }
        }
    }
}
