package ru.alterland.launcher.ui.screen.auth.recovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RecoveryScreen(
    viewModel: RecoveryScreenModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RecoveryContract.Effect.NavigateBack -> navigateBack()
        }
    }

    Recovery(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
