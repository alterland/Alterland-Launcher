package ru.alterland.launcher.ui.screen.main.servers.client

import ru.alterland.launcher.domain.model.clientprofile.ClientStatus
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class ClientContract {

    sealed class Event : UiEvent {
        data object OnPlayClicked: Event()
    }

    data class State(
        val status: ClientStatus = ClientStatus.Verification
    ): UiState

    sealed class Effect: UiEffect()
}
