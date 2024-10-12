package ru.alterland.launcher.ui.screen.auth.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.alterland.launcher.ui.screen.ContainerScreenProvider

class AuthContainerScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<AuthContainerScreenModel>()
        val state by screenModel.state.collectAsState()
        val effect by screenModel.effect.collectAsState(null)

        val navigator = LocalNavigator.currentOrThrow

        val dashboardContainer = rememberScreen(ContainerScreenProvider.Dashboard)

        when(effect) {
            is AuthContainerContract.Effect.OnNavigateToDashboard -> {
                navigator.push(dashboardContainer)
            }
            else -> {}
        }

        AuthContainer(
            state = state,
            setEvent = { e -> screenModel.setEvent(e) }
        )
    }
}
