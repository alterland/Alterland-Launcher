package ru.alterland.launcher.ui.screen.main.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.alterland.launcher.ui.screen.main.MainRoute
import ru.alterland.launcher.ui.screen.main.mainRouteConfig
import ru.alterland.launcher.ui.screen.main.servers.ServersScreen

@Composable
fun MainContainerScreen(
    viewModel: MainContainerViewModel = koinViewModel(),
    navigateToAuth: () -> Unit
) {
    val state by viewModel.collectAsState()

    val backStack = rememberNavBackStack(mainRouteConfig, MainRoute.Servers)

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainContainerContract.Effect.NavigateToAuth -> navigateToAuth()
        }
    }

    MainContainer(
        state = state,
        onAction = { viewModel.dispatch(it) }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<MainRoute.Servers> { ServersScreen() }
            }
        )
    }
}
