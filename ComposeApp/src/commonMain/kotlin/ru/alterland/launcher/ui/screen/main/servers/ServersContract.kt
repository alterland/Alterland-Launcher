package ru.alterland.launcher.ui.screen.main.servers

import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class ServersContract {
    sealed class Event : UiEvent {
        data class OnServerSelected(val page: Int): Event()
    }

    data class State(
        val currentServerProfile: ServerProfile? = null,
        val currentClientProfile: String? = null,
        val serversCount: Int = 0,
        val userStrength: Int = 0
    ): UiState

    sealed class Effect: UiEffect()
}
