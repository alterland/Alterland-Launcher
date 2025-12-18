package ru.alterland.launcher.ui.screen.main.servers

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsScreen
import ru.alterland.launcher.ui.screen.main.editserver.EditServerScreen
import ru.alterland.launcher.ui.screen.main.server.ServerScreen
import ru.alterland.launcher.ui.screen.main.skins.SkinsScreen

@Composable
fun ServersScreen(
    viewModel: ServersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    Servers(state = state) { pageBackStack ->
        NavDisplay(
            backStack = pageBackStack,
            entryProvider = entryProvider {
                entry<ServerRoute.Server> {
                    ServerScreen(payload = it.payload)
                }
                entry<ServerRoute.EditServer> {
                    EditServerScreen(
                        payload = it.payload,
                        navigateBack = { pageBackStack.removeLastOrNull() }
                    )
                }
                entry<ServerRoute.ClientSettings> {
                    ClientSettingsScreen(
                        payload = it.payload,
                        navigateBack = { pageBackStack.removeLastOrNull() }
                    )
                }
                entry<ServerRoute.Skins> {
                    SkinsScreen(
                        payload = it.payload,
                        navigateBack = { pageBackStack.removeLastOrNull() }
                    )
                }
            },
            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }
        )
    }
}
