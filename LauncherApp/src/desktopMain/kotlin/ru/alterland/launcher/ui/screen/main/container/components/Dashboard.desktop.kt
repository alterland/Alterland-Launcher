package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.io.IOException
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.screen.main.container.DashboardContract
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.AppleCircularProgressIndicator
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.errors.BaseErrorHandler
import ru.alterland.launcher.util.base.Resource

@Composable
actual fun Dashboard(
    state: DashboardContract.State,
    childNavigation: @Composable () -> Unit,
    onEvent: (e: DashboardContract.Event) -> Unit,
    navigateToAddServer: () -> Unit
) {
    val avatar = painterResource(Res.drawable.avatar_rofl)

    Row(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.backgroundTertiary)) {
        Column(Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.25f)
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp).fillMaxHeight().fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn {
                    items(state.miniServerItems) {
                        MiniServer(
                            item = it,
                            modifier = Modifier.padding(start = 9.dp, top = 12.dp, end = 11.dp)
                        )
                    }
                }
                MiniProfile(
                    user = state.user,
                    avatar = avatar,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 11.dp)
                ) {
                    onEvent(DashboardContract.Event.OnSignOutClicked)
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            when(state.servers) {
                is Resource.Content -> {
                    if (state.servers.data) {
                        childNavigation()
                    } else {
                        ServersPlaceholder(
                            isEmpty = true,
                            canAddServer =
                                state.user is Resource.Content<User> &&
                                (state.user.data.role?.strength ?: User.Role.DEFAULT_STRENGTH) >= User.Role.MIN_EDIT_STRENGTH,
                            onAddServerClick = { navigateToAddServer() },
                            onRetryClick = { onEvent(DashboardContract.Event.OnReload) }
                        )
                    }
                }
                is Resource.Error -> ServersPlaceholder(
                    throwable = state.servers.throwable,
                    onRetryClick = { onEvent(DashboardContract.Event.OnReload) }
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
                onMessageClose = { onEvent(DashboardContract.Event.OnMessageClose(it)) }
            )
        }
    }
}

@Composable
private fun ServersPlaceholder(
    throwable: Throwable? = null,
    isEmpty: Boolean = false,
    canAddServer: Boolean = false,
    onAddServerClick: () -> Unit = {},
    onRetryClick: () -> Unit = {}
) {
    val descriptionText = when {
        throwable is IOException -> Res.string.no_internet
        isEmpty -> Res.string.just_empty
        else -> Res.string.unknown_exception
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.servers_empty_placeholder_title),
            color = AppTheme.colors.labelSecondary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(descriptionText),
            color = AppTheme.colors.labelTertiary,
            textAlign = TextAlign.Center
        )
        Button(
            text = stringResource(Res.string.retry),
            onClick = onRetryClick,
            backgroundColor = AppTheme.colors.backgroundElevatedTertiary,
            modifier = Modifier
                .padding(top = 14.dp)
                .width(155.dp)
                .height(35.dp)
        )
        if (canAddServer) {
            Button(
                text = stringResource(Res.string.just_empty),
                onClick = onAddServerClick,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(155.dp)
                    .height(35.dp)
            )
        }
    }
}
