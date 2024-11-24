package ru.alterland.launcher.ui.screen.main.serverinfo

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.ServerProfile

class ServerInfoContract {
    sealed class Event : UiEvent {
        data class OnServerSelected(val page: Int): Event()
        data class OnPlayClicked(val clientProfile: ClientProfile): Event()
    }

    data class State(
        val currentServerProfile: ServerProfile? = null,
        val currentClientProfile: ClientProfile? = null,
        val isFetchingClientProfile: Boolean = false,
        val serversCount: Int = 0
    ): UiState

    sealed class Effect: UiEffect()
}
