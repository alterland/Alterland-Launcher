package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launcher.domain.model.AppError
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.ui.screen.main.container.components.MiniServerItem
import ru.alterland.launcher.util.base.Resource

class DashboardContract {
    sealed class Event : UiEvent {
        data object OnReload: Event()
        data class OnMessageClose(val id: String): Event()
        data object OnSignOutClicked: Event()
    }

    data class State(
        val errors: List<AppError> = listOf(),
        val currentClient: String? = null,
        val miniServerItems: List<MiniServerItem> = listOf(),
        val isClientServiceOffline: Boolean = false,

        val user: Resource<User>? = null,
        val servers: Resource<Boolean>? = null,
    ): UiState

    sealed class Effect: UiEffect() {
        data object OnNavigateToAuth: Effect()
    }
}
