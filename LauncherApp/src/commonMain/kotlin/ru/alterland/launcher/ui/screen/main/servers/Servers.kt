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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.screen.main.MainScreenProvider
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.editserver.EditServerScreen
import ru.alterland.launcher.ui.screen.main.server.ServerPayload
import ru.alterland.launcher.ui.screen.main.servers.ServerTab.*
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload
import ru.alterland.launcher.ui.theme.AppTheme
import kotlin.uuid.ExperimentalUuidApi

@OptIn(InternalVoyagerApi::class, ExperimentalUuidApi::class)
@Composable
fun Servers(state: ServersContract.State) {

    VerticalPager(
        state = rememberPagerState(pageCount = { state.serverProfiles.size }),
        modifier = Modifier.fillMaxSize()
    ) { page ->
        state.serverProfiles.getOrNull(page)?.let { serverProfile ->
            val tabs = mutableListOf<Tab>(
                PlayTab(
                    tabOptions = TabOptions(
                        index = 0u,
                        title = stringResource(Res.string.play)
                    ),
                    screen = rememberScreen(
                        provider = MainScreenProvider.Server(
                            payload = ServerPayload(serverProfile = serverProfile)
                        )
                    )
                ),
                SkinsTab(
                    tabOptions = TabOptions(
                        index = 1u,
                        title = stringResource(Res.string.play)
                    ),
                    screen = rememberScreen(
                        provider = MainScreenProvider.Skins
                    )
                )
            ).apply {
                var i: UShort = 2u
                serverProfile.clientProfile?.let { id ->
                    add(
                        ClientSettingsTab(
                            tabOptions = TabOptions(
                                index = i,
                                title = stringResource(Res.string.settings)
                            ),
                            screen = rememberScreen(
                                provider = MainScreenProvider.ClientSettings(
                                    payload = ClientPayload(id = id)
                                )
                            )
                        )
                    )
                    i++
                }
                if (state.userStrength >= User.Role.MIN_EDIT_STRENGTH) {
                    add(
                        EditServerTab(
                            tabOptions = TabOptions(
                                index = i,
                                title = stringResource(Res.string.edit)
                            ),
                            screen = EditServerScreen(
                                payload = EditServerPayload.Edit(serverProfile = serverProfile)
                            )
                        )
                    )
                    i++
                    add(
                        AddServerTab(
                            tabOptions = TabOptions(
                                index = i,
                                title = stringResource(Res.string.add)
                            ),
                            screen = EditServerScreen(payload = EditServerPayload.Add)
                        )
                    )
                    i++
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
                TabNavigator(tabs.first()) { tabNavigator ->
                    Row(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
                        tabs.forEach { tab -> TabItem(tab) }
                    }
                    CurrentTab()
                }
            }
        }
    }
}
