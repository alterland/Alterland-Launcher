package ru.alterland.launcher.ui.screen.main.server

import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.ui.base.BaseViewModel

class ServerViewModel(
    private val payload: ServerPayload
): BaseViewModel<ServerContract.State, ServerContract.Effect, ServerContract.Action>() {

    override val container = container<ServerContract.State, ServerContract.Effect>(
        initialState = ServerContract.State(serverProfile = payload.serverProfile)
    )

    override fun dispatch(action: ServerContract.Action) {}
}
