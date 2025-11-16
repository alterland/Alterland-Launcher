package ru.alterland.launcher.ui.screen.main.editserver

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EditServerScreen(
    payload: EditServerPayload,
    navigateBack: () -> Unit
) {
    val viewModelKey = when(payload.mode) {
        EditServerMode.Add -> null
        is EditServerMode.Edit -> payload.mode.serverProfile.id
    }
    val viewModel: EditServerViewModel = koinViewModel(key = viewModelKey) { parametersOf(payload) }
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            EditServerContract.Effect.NavigateBack -> navigateBack()
        }
    }

    EditServer(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
