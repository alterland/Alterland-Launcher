package ru.alterland.launcher.ui.screen.auth.recovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class RecoveryScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<RecoveryScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        Recovery(
            state = state,
            setEvent = { e -> screenModel.setEvent(e) },
            navigateBack = { navigator.pop() }
        )
    }
}
