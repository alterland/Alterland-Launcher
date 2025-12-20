package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.domain.model.Store
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class ClientSettingsContract {

    sealed class Action: UiAction {
        data object OnLaunchAfterUpdateClicked : Action()
        data object OnLaunchFullscreenClicked : Action()
        data object OnAutoConnectClicked : Action()
        data class OnRamInput(val value: String) : Action()
        data object OnRamInputFinished : Action()
    }

    data class State(
        val settings: Store.MinecraftSettings? = null,
        val ramValue: String = "",
        val recommendedRam: Int
    ) : UiState

    sealed class Effect: UiEffect {
    }
}
