package ru.alterland.launcher.ui.screen.auth.recovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class RecoveryScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<RecoveryScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        Recovery(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) },
            navigateBack = { navigator.pop() }
        )
    }
}
