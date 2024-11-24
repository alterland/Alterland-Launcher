package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launcher.domain.entity.AppError
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class DashboardContract {
    sealed class Event : UiEvent {
        data class OnMessageClose(val id: String): Event()
        data object OnSignOutClicked: Event()
    }

    data class State(
        val errors: List<AppError> = listOf(),
        val currentClient: String? = null,
        val nickName: String = "",
        val menuItems: List<MenuItem> = listOf(),
        val isClientServiceOffline: Boolean = false
    ): UiState

    sealed class Effect: UiEffect() {
        data object OnNavigateToAuth: Effect()
    }
}
