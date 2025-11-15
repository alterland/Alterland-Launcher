package ru.alterland.launcher.ui.screen.main.client

import ru.alterland.launcher.domain.model.clientprofile.ClientStatus
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class ClientContract {

    sealed class Action : UiAction {
        data object OnPlayClicked: Action()
    }

    data class State(
        val status: ClientStatus = ClientStatus.Verification
    ): UiState

    sealed class Effect: UiEffect
}
