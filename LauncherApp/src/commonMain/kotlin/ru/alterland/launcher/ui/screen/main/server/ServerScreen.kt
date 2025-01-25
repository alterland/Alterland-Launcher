package ru.alterland.launcher.ui.screen.main.server

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import org.koin.core.parameter.parameterSetOf
import ru.alterland.launcher.ui.screen.main.MainScreenProvider
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload

class ServerScreen(
    private val payload: ServerPayload,
    override val key: ScreenKey = "ServerScreen_${payload.serverProfile.id}"
): Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ServerScreenModel>(parameters = { parameterSetOf(payload) })
        val state by screenModel.state.collectAsState()

        val clientScreen = state.serverProfile.clientProfile?.let {
            ScreenRegistry.get(MainScreenProvider.Client(payload = ClientPayload(id = it)))
        }

        Server(
            state = state,
            clientNavigation = { clientScreen?.let { Navigator(screen = it) } },
        )
    }
}
