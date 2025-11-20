package ru.alterland.launcher.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    navigateToAuth: () -> Unit,
    navigateToMain: () -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SplashContract.Effect.NavigateToAuth -> navigateToAuth()
            SplashContract.Effect.NavigateToMain -> navigateToMain()
        }
    }

    Splash(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
