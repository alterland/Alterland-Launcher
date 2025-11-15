package ru.alterland.launcher.ui.screen.main.server

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import ru.alterland.launcher.ui.screen.main.client.ClientPayload
import ru.alterland.launcher.ui.screen.main.client.ClientScreen

@Composable
fun ServerScreen(
    payload: ServerPayload,
    viewModel: ServerViewModel = koinViewModel { parametersOf(payload) }
) {
    val state by viewModel.collectAsState()

    Server(
        state = state,
        client = {
            state.serverProfile.clientProfile?.let {
                ClientScreen(payload = ClientPayload(id = it))
            }
        },
    )
}
