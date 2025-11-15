package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.ui.model.AppErrorUi
import ru.alterland.launcher.ui.screen.main.container.components.MiniServerItem
import ru.alterland.launcher.util.base.Resource

class MainContainerContract {
    sealed class Action : UiAction {
        data object OnReload: Action()
        data class OnMessageClose(val id: String): Action()
        data object OnSignOutClicked: Action()
        data object OnNavigateToAddServer: Action()
    }

    data class State(
        val errors: List<AppErrorUi> = emptyList(),
        val currentClient: String? = null,
        val miniServerItems: List<MiniServerItem> = emptyList(),
        val isClientServiceOffline: Boolean = false,

        val user: Resource<User>? = null,
        val servers: Resource<Boolean>? = null,
    ): UiState

    sealed class Effect: UiEffect {
        data object NavigateToAuth: Effect()
    }
}
