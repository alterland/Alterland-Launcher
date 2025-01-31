package ru.alterland.launcher.ui.screen.main.skins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel

class SkinsScreen: Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SkinsScreenModel>()
        val state by screenModel.state.collectAsState()


        Skins(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) },
        )
    }
}