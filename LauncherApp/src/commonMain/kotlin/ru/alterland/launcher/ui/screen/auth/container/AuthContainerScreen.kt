package ru.alterland.launcher.ui.screen.auth.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.alterland.launcher.ui.screen.auth.AuthRoute
import ru.alterland.launcher.ui.screen.auth.authRouteConfig
import ru.alterland.launcher.ui.screen.auth.recovery.RecoveryScreen
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInScreen
import ru.alterland.launcher.ui.screen.auth.sign_up.SignUpScreen

@Composable
fun AuthContainerScreen(
    viewModel: AuthContainerViewModel = koinViewModel(),
    navigateToMain: () -> Unit
) {
    val state by viewModel.collectAsState()

    val backStack = rememberNavBackStack(authRouteConfig, AuthRoute.SignIn)

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AuthContainerContract.Effect.NavigateToMain -> navigateToMain()
        }
    }

    AuthContainer(
        state = state,
        onAction = { viewModel.dispatch(it) }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<AuthRoute.SignIn> {
                    SignInScreen(
                        navigateToSignUp = { backStack.add(AuthRoute.SignUp) },
                        navigateToRecovery = { backStack.add(AuthRoute.Recovery) }
                    )
                }
                entry<AuthRoute.SignUp> {
                    SignUpScreen(
                        navigateBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<AuthRoute.Recovery> {
                    RecoveryScreen(
                        navigateBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}
