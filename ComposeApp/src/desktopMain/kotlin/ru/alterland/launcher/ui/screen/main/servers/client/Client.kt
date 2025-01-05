package ru.alterland.launcher.ui.screen.main.servers.client

import androidx.compose.runtime.Composable

@Composable
fun Client(
    state: ClientContract.State,
    onEvent: (e: ClientContract.Event) -> Unit
) {
    PlayButton(clientStatus = state.status) {
        onEvent(ClientContract.Event.OnPlayClicked)
    }
}
