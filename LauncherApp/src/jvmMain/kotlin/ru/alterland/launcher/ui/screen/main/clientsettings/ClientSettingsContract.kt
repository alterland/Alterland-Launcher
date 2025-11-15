package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class ClientSettingsContract {

    data object Action: UiAction

    class State() : UiState

    data object Effect: UiEffect
}
