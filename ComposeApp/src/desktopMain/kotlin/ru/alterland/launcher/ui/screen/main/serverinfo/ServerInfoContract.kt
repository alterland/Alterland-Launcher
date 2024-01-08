package ru.alterland.launcher.ui.screen.main.serverinfo

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.ServerProfile

class ServerInfoContract {
    sealed class Event : UiEvent {
        data class OnPlayClicked(val profile: ServerProfile): Event()
    }

    data class State(
        val servers: List<ServerProfile> = listOf(),
        val clients: List<ClientProfile> = listOf()
    ): UiState

    sealed class Effect: UiEffect
}
