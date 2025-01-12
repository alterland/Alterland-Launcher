package ru.alterland.launcher.ui.screen.main.clientsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

actual class ClientSettingsScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ClientSettingsScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        ClientSettings(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) },
            navigateBack = { navigator.pop() }
        )
    }
}
