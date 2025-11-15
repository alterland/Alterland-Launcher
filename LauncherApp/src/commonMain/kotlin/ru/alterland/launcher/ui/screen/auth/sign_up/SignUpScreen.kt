package ru.alterland.launcher.ui.screen.auth.sign_up

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    viewModel: SignUpScreenModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SignUpContract.Effect.NavigateBack -> navigateBack()
            SignUpContract.Effect.ShowToastSocialsSignInNotYetDone -> {}
        }
    }

    SignUp(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
