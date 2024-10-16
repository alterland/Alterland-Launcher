package ru.alterland.launcher.ui.screen.main.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import ru.alterland.launcher.Res
import ru.alterland.launcher.ui.screen.main.clients.MenuClientsList
import ru.alterland.launcher.ui.screen.main.container.miniprofile.MiniProfile
import ru.alterland.launcher.ui.screen.main.serverinfo.ServerInfoScreen
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.errors.BaseErrorHandler

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun Dashboard(
    state: DashboardContract.State,
    setEvent: (e: DashboardContract.Event) -> Unit
) {
    val avatar = painterResource(Res.image.avatar_rofl)

    Row(Modifier.fillMaxWidth()) {
        Column(Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.25f)
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp).fillMaxHeight().fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MenuClientsList(
                    items = state.menuItems,
                    modifier = Modifier.padding(start = 9.dp, top = 12.dp, end = 11.dp)
                )
                MiniProfile(
                    nickName = state.nickName,
                    avatar = avatar,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 11.dp)
                ) {
                    setEvent(DashboardContract.Event.OnSignOutClicked)
                }
            }
        }
        Box {
            Navigator(screen = ServerInfoScreen()) { navigator ->
                SlideTransition(
                    navigator = navigator,
                    orientation = SlideOrientation.Vertical,
                    disposeScreenAfterTransitionEnd = true
                )
            }
            if (state.isClientServiceOffline) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().background(AppTheme.colors.red).padding(4.dp)
                ) {
                    Text(
                        text = "Сервер синхронизации файлов игры временно недоступен",
                        color = AppTheme.colors.forceWhitePrimary
                    )
                }
            }
            BaseErrorHandler(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(320.dp)
                    .fillMaxHeight(0.5f)
                    .padding(top = 18.dp, end = 16.dp),
                itemsModifier = Modifier.padding(vertical = 3.dp),
                errors = state.errors,
                onMessageClose = { setEvent(DashboardContract.Event.OnMessageClose(it)) }
            )
        }
    }
}
