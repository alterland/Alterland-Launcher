package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class DashboardContract {
    sealed class Event : UiEvent {
        data object OnSignOutClicked: Event()
    }

    data class State(
        val currentClient: String? = null,
        val nickName: String = "",
        val menuItems: List<MenuItem> = listOf(),
        val isClientServiceOffline: Boolean = false
    ): UiState

    sealed class Effect: UiEffect {
        data object OnNavigateToAuth: Effect()
    }
}
