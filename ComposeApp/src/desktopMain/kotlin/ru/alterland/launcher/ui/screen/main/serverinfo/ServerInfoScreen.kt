package ru.alterland.launcher.ui.screen.main.serverinfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.alterland.launcher.ui.screen.main.MainScreenProvider

class ServerInfoScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ServerInfoScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val clientSettingsScreen = rememberScreen(MainScreenProvider.ClientSettings)

        ServerInfo(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) },
            navigateToClientSettings = { navigator.push(clientSettingsScreen) }
        )
    }
}
