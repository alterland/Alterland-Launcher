package ru.alterland.launcher.ui.screen.auth.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        val effects by screenModel.effects.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        val dashboardContainer = rememberScreen(ContainerScreenProvider.Dashboard)

        effects.firstOrNull()?.let { effect ->
            LaunchedEffect(effect) {
                when (effect) {
                    AuthContainerContract.Effect.OnNavigateToDashboard -> {
                        navigator.push(dashboardContainer)
                    }
                }
            }
            screenModel.onEffectHandled(effect)
        }

        AuthContainer(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) }
        )
    }
}
