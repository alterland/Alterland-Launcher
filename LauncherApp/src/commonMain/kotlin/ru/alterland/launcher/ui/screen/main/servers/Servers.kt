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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsPayload
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsScreen
import ru.alterland.launcher.ui.screen.main.editserver.EditServerMode
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.editserver.EditServerScreen
import ru.alterland.launcher.ui.screen.main.server.ServerPayload
import ru.alterland.launcher.ui.screen.main.server.ServerScreen
import ru.alterland.launcher.ui.screen.main.servers.components.TabItem
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun Servers(
    state: ServersContract.State
) {
    VerticalPager(
        state = rememberPagerState(pageCount = { state.serverProfiles.size }),
        modifier = Modifier.fillMaxSize()
    ) { page ->
        state.serverProfiles.getOrNull(page)?.let { serverProfile ->

            val serverPayload = ServerPayload(serverProfile = serverProfile)
            val serverTab = ServerTab.Server(payload = serverPayload)

            val backStack = rememberNavBackStack(
                serverRouteConfig,
                ServerRoute.Server(payload = serverPayload)
            )

            var currentTab by rememberSaveable { mutableStateOf<ServerTab>(serverTab) }

            val tabs = mutableListOf<ServerTab>(serverTab).apply {
                serverProfile.clientProfile?.let { id ->
                    add(ServerTab.ClientSettings(payload = ClientSettingsPayload(id = id)))
                }
                if (state.userStrength >= User.Role.MIN_EDIT_STRENGTH) {
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
                                action = { backStack.add(ServerRoute.Server(payload = tab.payload)) }
                            }
                            is ServerTab.ClientSettings -> {
                                title = stringResource(Res.string.settings)
                                action = { backStack.add(ServerRoute.ClientSettings(payload = tab.payload)) }
                            }
                            is ServerTab.EditServer -> {
                                title = stringResource(Res.string.edit)
                                action = { backStack.add(ServerRoute.EditServer(payload = tab.payload)) }
                            }
                            is ServerTab.AddServer -> {
                                title = stringResource(Res.string.add)
                                action = { backStack.add(ServerRoute.EditServer(payload = tab.payload)) }
                            }
                        }
                        TabItem(
                            title = title,
                            isSelected = currentTab == tab,
                            onClick = {
                                action()
                                currentTab = tab
                            }
                        )
                    }
                }
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<ServerRoute.Server> {
                            ServerScreen(payload = it.payload)
                        }
                        entry<ServerRoute.EditServer> {
                            EditServerScreen(
                                payload = it.payload,
                                navigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                        entry<ServerRoute.ClientSettings> {
                            ClientSettingsScreen(
                                payload = it.payload,
                                navigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }
        }
    }
}
