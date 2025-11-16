package ru.alterland.launcher.ui.screen.main.clientsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
actual fun ClientSettingsScreen(
    payload: ClientSettingsPayload,
    navigateBack: () -> Unit
) {
    val viewModel: ClientSettingsViewModel = koinViewModel(key = payload.id) { parametersOf(payload) }
    val state by viewModel.collectAsState()

    ClientSettings(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
