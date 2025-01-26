package ru.alterland.launcher.ui.screen.main.clientsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload

actual class ClientSettingsScreen actual constructor(private val payload: ClientPayload): Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ClientSettingsScreenModel>()
        val state by screenModel.state.collectAsState()

        ClientSettings(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) }
        )
    }
}
