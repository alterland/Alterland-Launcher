package ru.alterland.launcher.ui.screen.main.servers

import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class ServersContract {
    sealed class Action : UiAction

    data class State(
        val serverProfiles: List<ServerProfile> = listOf(),
        val userStrength: Int = 0
    ): UiState

    sealed class Effect: UiEffect
}
