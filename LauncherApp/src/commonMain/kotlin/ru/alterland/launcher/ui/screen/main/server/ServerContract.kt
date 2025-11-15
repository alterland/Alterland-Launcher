package ru.alterland.launcher.ui.screen.main.server

import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class ServerContract {

    sealed class Action : UiAction

    data class State(
        val serverProfile: ServerProfile
    ): UiState

    sealed class Effect: UiEffect
}
