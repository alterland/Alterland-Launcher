package ru.alterland.launcher.ui.screen.auth.container

import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.ui.model.AppErrorUi

sealed class AuthContainerContract {

    sealed class Action : UiAction {
        data class OnMessageClose(val id: String): Action()
    }

    data class State(
        val errors: List<AppErrorUi> = emptyList()
    ): UiState

    sealed class Effect : UiEffect {
        data object NavigateToMain: Effect()
    }
}
