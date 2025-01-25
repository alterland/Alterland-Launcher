package ru.alterland.launcher.ui.screen.main.servers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel

class ServersScreen: Screen {

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ServersScreenModel>()
        val state by screenModel.state.collectAsState()

        Servers(state = state)
    }
}
