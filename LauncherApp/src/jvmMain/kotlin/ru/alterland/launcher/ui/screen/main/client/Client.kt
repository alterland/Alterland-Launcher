package ru.alterland.launcher.ui.screen.main.client

import androidx.compose.runtime.Composable
import ru.alterland.launcher.ui.screen.main.client.components.PlayButton

@Composable
fun Client(
    state: ClientContract.State,
    onAction: (ClientContract.Action) -> Unit
) {
    PlayButton(clientStatus = state.status) {
        onAction(ClientContract.Action.OnPlayClicked)
    }
}
