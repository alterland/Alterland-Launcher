package ru.alterland.launcher.ui.screen.main.servers.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import org.koin.core.parameter.parameterSetOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual class ClientScreen actual constructor(
    private val payload: ClientPayload
): Screen {

    @OptIn(ExperimentalUuidApi::class)
    override val key: ScreenKey = Uuid.random().toString()

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ClientScreenModel>(parameters = { parameterSetOf(payload) })
        val state by screenModel.state.collectAsState()

        Client(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) }
        )
    }
}
