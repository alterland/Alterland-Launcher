package ru.alterland.launcher.ui.screen.auth.sign_in

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignInScreen(
    viewModel: SignInScreenModel = koinViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToRecovery: () -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SignInContract.Effect.NavigateToSignUp -> navigateToSignUp()
            SignInContract.Effect.NavigateToRecovery -> navigateToRecovery()
            SignInContract.Effect.ShowToastSocialsSignInNotYetDone -> {

            }
        }
    }

    SignIn(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
