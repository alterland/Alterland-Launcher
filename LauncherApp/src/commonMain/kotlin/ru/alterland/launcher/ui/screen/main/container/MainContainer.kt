package ru.alterland.launcher.ui.screen.main.container

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.avatar_rofl
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.model.ServerProfileWithStatus
import ru.alterland.launcher.ui.screen.main.container.components.MiniProfile
import ru.alterland.launcher.ui.screen.main.container.components.MiniServer
import ru.alterland.launcher.ui.screen.main.container.components.ServersPlaceholder
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.AppleCircularProgressIndicator
import ru.alterland.launcher.ui.widgets.errors.BaseErrorHandler
import ru.alterland.launcher.util.base.Resource

@Composable
fun MainContainer(
    state: MainContainerContract.State,
    onAction: (MainContainerContract.Action) -> Unit,
    childNavigation: @Composable () -> Unit
) {
    val avatar = painterResource(Res.drawable.avatar_rofl)

    Row(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.backgroundTertiary)) {
        Column(Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.25f)
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.serverProfiles is Resource.Content) {
                    LazyColumn {
                        items(state.serverProfiles.data) {
                            MiniServer(
                                item = it,
                                modifier = Modifier.padding(start = 9.dp, top = 12.dp, end = 11.dp)
                            )
                        }
                    }
                }
                MiniProfile(
                    user = state.user,
                    avatar = avatar,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 11.dp),
                    onExit = { onAction(MainContainerContract.Action.OnSignOutClicked) }
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            when(state.serverProfiles) {
                is Resource.Content -> {
                    if (state.serverProfiles.data.isNotEmpty()) {
                        childNavigation()
                    } else {
                        ServersPlaceholder(
                            isEmpty = true,
                            canAddServer =
                                state.user is Resource.Content<User> &&
                                        (state.user.data.role?.strength ?: User.Role.DEFAULT_STRENGTH) >= User.Role.MIN_EDIT_STRENGTH,
                            onAddServerClick = { onAction(MainContainerContract.Action.OnNavigateToAddServer) },
                            onRetryClick = { onAction(MainContainerContract.Action.OnReload) }
                        )
                    }
                }
                is Resource.Error -> ServersPlaceholder(
                    throwable = state.serverProfiles.throwable,
                    onRetryClick = { onAction(MainContainerContract.Action.OnReload) }
                )
                is Resource.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AppleCircularProgressIndicator(
                        color = AppTheme.colors.labelSecondary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(32.dp)
                    )
                }
                null -> {}
            }
            BaseErrorHandler(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(320.dp)
                    .fillMaxHeight(0.5f)
                    .padding(top = 18.dp, end = 16.dp),
                itemsModifier = Modifier.padding(vertical = 3.dp),
                errors = state.errors,
                onErrorClose = { onAction(MainContainerContract.Action.OnMessageClose(it)) }
            )
        }
    }
}
