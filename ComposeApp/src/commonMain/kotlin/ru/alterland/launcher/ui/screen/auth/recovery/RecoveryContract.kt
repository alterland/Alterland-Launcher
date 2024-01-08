package ru.alterland.launcher.ui.screen.auth.recovery

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class RecoveryContract {

    sealed class Event : UiEvent {
        data class OnEmailInput(val data: String): Event()
        data object OnResetPasswordClicked: Event()
    }

    data class State(
        val email: String = "",
        val sendCodeProgress: Boolean = false,
    ): UiState

    sealed class Effect: UiEffect
}
