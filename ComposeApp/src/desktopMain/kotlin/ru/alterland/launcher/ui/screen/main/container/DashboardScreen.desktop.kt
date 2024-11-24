package ru.alterland.launcher.ui.screen.main.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.alterland.launcher.ui.screen.main.mainScreenModule

actual class DashboardScreen: Screen {

    @Composable
    override fun Content() {

        ScreenRegistry {
            mainScreenModule()
        }

        val screenModel = koinScreenModel<DashboardScreenModel>()
        val state by screenModel.state.collectAsState()
        val effects by screenModel.effects.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        effects.firstOrNull()?.let { effect ->
            LaunchedEffect(effect) {
                when (effect) {
                    DashboardContract.Effect.OnNavigateToAuth -> navigator.popAll()
                }
            }
            screenModel.onEffectHandled(effect)
        }

        Dashboard(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) }
        )
    }
}
