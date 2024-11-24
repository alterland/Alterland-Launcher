package ru.alterland.launcher.ui.screen.auth.container

import ru.alterland.launcher.domain.entity.AppError
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class AuthContainerContract {

    sealed class Event : UiEvent {
        data class OnMessageClose(val id: String): Event()
    }

    data class State(
        val errors: List<AppError> = listOf()
    ): UiState

    sealed class Effect: UiEffect() {
        data object OnNavigateToDashboard: Effect()
    }
}
