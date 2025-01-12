package ru.alterland.launcher.ui.screen.main.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import ru.alterland.launcher.ui.screen.main.MainScreenProvider
import ru.alterland.launcher.ui.screen.main.container.components.Dashboard
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.mainScreenModule
import ru.alterland.launcher.ui.screen.main.servers.ServersScreen

class DashboardScreen: Screen {

    @OptIn(ExperimentalVoyagerApi::class)
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
            onEvent = { e -> screenModel.onEvent(e) },
            childNavigation = {
                Navigator(screen = ServersScreen()) { childNavigator ->
                    SlideTransition(
                        navigator = childNavigator,
                        orientation = SlideOrientation.Vertical,
                        disposeScreenAfterTransitionEnd = true
                    )
                }
            },
            navigateToAddServer = {
                val screen = ScreenRegistry.get(MainScreenProvider.EditServer(payload = EditServerPayload.Add))
                navigator.push(screen)
            }
        )
    }
}
