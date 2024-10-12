package ru.alterland.launcher.ui.screen.main.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

actual class DashboardScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DashboardScreenModel>()
        val state by screenModel.state.collectAsState()
        val effect by screenModel.effect.collectAsState(null)

        val navigator = LocalNavigator.currentOrThrow

        when(effect) {
            DashboardContract.Effect.OnNavigateToAuth -> {
                navigator.popAll()
            }
            else -> {}
        }

        Dashboard(
            state = state,
            setEvent = { e -> screenModel.setEvent(e) }
        )
    }
}
