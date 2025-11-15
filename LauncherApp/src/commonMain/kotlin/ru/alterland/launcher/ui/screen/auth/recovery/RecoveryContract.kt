package ru.alterland.launcher.ui.screen.auth.recovery

import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class RecoveryContract {

    sealed class Action : UiAction {
        data class OnEmailInput(val data: String): Action()
        data object OnResetPasswordClick: Action()
        data object OnBackClick: Action()
    }

    data class State(
        val email: String = "",
        val sendCodeProgress: Boolean = false,
    ): UiState

    sealed class Effect: UiEffect {
        data object NavigateBack: Effect()
    }
}
