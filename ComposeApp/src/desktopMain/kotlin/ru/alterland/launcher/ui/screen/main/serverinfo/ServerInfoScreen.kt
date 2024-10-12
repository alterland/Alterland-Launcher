package ru.alterland.launcher.ui.screen.main.serverinfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel

class ServerInfoScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ServerInfoScreenModel>()
        val state by screenModel.state.collectAsState()
        val effect by screenModel.effect.collectAsState(null)

        ServerInfo(
            state = state,
            setEvent = { e -> screenModel.setEvent(e) }
        )
    }
}