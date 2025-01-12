package ru.alterland.launcher.ui.screen.main.servers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.alterland.launcher.ui.screen.main.MainScreenProvider
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload

class ServersScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ServersScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        val clientScreen = state.currentClientProfile?.let {
            ScreenRegistry.get(MainScreenProvider.Client(payload = ClientPayload(id = it)))
        }
        val clientSettingsScreen = rememberScreen(MainScreenProvider.ClientSettings)

        Servers(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) },
            clientNavigation = { clientScreen?.let { Navigator(screen = it) } },
            navigateToClientSettings = { navigator.push(clientSettingsScreen) },
            navigateToEditServer = {
                val payload = EditServerPayload.Edit(serverProfileId = it)
                val screen = ScreenRegistry.get(MainScreenProvider.EditServer(payload = payload))
                navigator.push(screen)
            }
        )
    }
}
