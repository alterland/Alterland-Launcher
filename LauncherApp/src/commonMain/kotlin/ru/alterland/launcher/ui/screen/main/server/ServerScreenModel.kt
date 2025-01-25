package ru.alterland.launcher.ui.screen.main.server

import ru.alterland.launcher.ui.base.BaseScreenModel

class ServerScreenModel(
    private val payload: ServerPayload
): BaseScreenModel<ServerContract.Event, ServerContract.State, ServerContract.Effect>(
    initialState = ServerContract.State(serverProfile = payload.serverProfile)
) {
    override fun onEvent(event: ServerContract.Event) {}
}
