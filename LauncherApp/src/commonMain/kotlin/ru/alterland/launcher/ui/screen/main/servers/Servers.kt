package ru.alterland.launcher.ui.screen.main.servers

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.Role
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsPayload
import ru.alterland.launcher.ui.screen.main.editserver.EditServerMode
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.server.ServerPayload
import ru.alterland.launcher.ui.screen.main.servers.components.TabItem
import ru.alterland.launcher.ui.screen.main.skins.SkinsPayload
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun Servers(
    state: ServersContract.State,
    tabNavigation: @Composable (NavBackStack<NavKey>) -> Unit
) {
    var isOnPlayTab by rememberSaveable { mutableStateOf(true) }

    VerticalPager(
        state = rememberPagerState(pageCount = { state.serverProfiles.size }),
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = isOnPlayTab,
        key = { index -> state.serverProfiles.getOrNull(index)?.id ?: index }
    ) { page ->
        state.serverProfiles.getOrNull(page)?.let { serverProfile ->
            val serverPayload = ServerPayload(serverProfile = serverProfile)
            val serverTab = ServerTab.Server(payload = serverPayload)

            val pageBackStack = rememberNavBackStack(
                serverRouteConfig,
                ServerRoute.Server(payload = serverPayload)
            )

            var currentTab by remember(serverProfile.id) { mutableStateOf<ServerTab>(serverTab) }

            val tabs = mutableListOf<ServerTab>(serverTab).apply {
                serverProfile.clientProfile?.let { id ->
                    add(ServerTab.ClientSettings(payload = ClientSettingsPayload(id = id)))
                }
                add(ServerTab.Skins(payload = SkinsPayload(serverProfile = serverProfile)))
                if (state.user.getOrNull()?.role == Role.ADMIN) {
                    add(
                        ServerTab.EditServer(
                            payload = EditServerPayload(
                                mode = EditServerMode.Edit(
                                    serverProfile = serverProfile
                                )
                            )
                        )
                    )
                    add(ServerTab.AddServer(payload = EditServerPayload(mode = EditServerMode.Add)))
                }
            }

            LaunchedEffect(Unit) {
                snapshotFlow { pageBackStack.lastOrNull() }
                    .collect { currentRoute ->
                        val newTab = when (currentRoute) {
                            is ServerRoute.Server -> serverTab
                            is ServerRoute.ClientSettings -> tabs.find { it is ServerTab.ClientSettings }
                            is ServerRoute.Skins -> tabs.find { it is ServerTab.Skins }
                            is ServerRoute.EditServer -> {
                                when (currentRoute.payload.mode) {
                                    is EditServerMode.Edit -> tabs.find { it is ServerTab.EditServer }
                                    is EditServerMode.Add -> tabs.find { it is ServerTab.AddServer }
                                }
                            }
                            else -> serverTab
                        } ?: serverTab
                        currentTab = newTab
                        isOnPlayTab = newTab is ServerTab.Server
                    }
            }

            Column(modifier = Modifier.fillMaxSize().background(AppTheme.colors.backgroundElevatedSecondary)) {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                    text = serverProfile.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AppTheme.colors.labelPrimary
                )
                Row(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
                    tabs.forEach { tab ->
                        var title: String
                        var action: () -> Unit
                        when(tab) {
                            is ServerTab.Server -> {
                                title = stringResource(Res.string.play)
                                action = { pageBackStack.add(ServerRoute.Server(payload = tab.payload)) }
                            }
                            is ServerTab.ClientSettings -> {
                                title = stringResource(Res.string.settings)
                                action = { pageBackStack.add(ServerRoute.ClientSettings(payload = tab.payload)) }
                            }
                            is ServerTab.Skins -> {
                                title = stringResource(Res.string.skins)
                                action = { pageBackStack.add(ServerRoute.Skins(payload = tab.payload)) }
                            }
                            is ServerTab.EditServer -> {
                                title = stringResource(Res.string.edit)
                                action = { pageBackStack.add(ServerRoute.EditServer(payload = tab.payload)) }
                            }
                            is ServerTab.AddServer -> {
                                title = stringResource(Res.string.add)
                                action = { pageBackStack.add(ServerRoute.EditServer(payload = tab.payload)) }
                            }
                        }
                        TabItem(
                            title = title,
                            isSelected = currentTab == tab,
                            onClick = { action() }
                        )
                    }
                }
                tabNavigation(pageBackStack)
            }
        }
    }
}
