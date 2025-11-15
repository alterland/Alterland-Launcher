package ru.alterland.launcher.ui.screen.main.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
actual fun ClientScreen(payload: ClientPayload) {
    val viewModel: ClientViewModel = koinViewModel { parametersOf(payload) }
    val state by viewModel.collectAsState()

    Client(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
