package ru.alterland.launcher.ui.screen.splash

import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class SplashContract {
    sealed class Action : UiAction

    class State(

    ): UiState

    sealed class Effect: UiEffect {
        data object NavigateToAuth: Effect()
        data object NavigateToMain: Effect()
    }
}
