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
        data class OnRamSliderValueChange(val value: Float) : Action()
        data object OnRamSliderValueChangeFinished : Action()
    }

    data class State(
        val settings: Store.MinecraftSettings? = null,
        val ramSlider: Slider? = null
    ) : UiState {
        data class Slider(
            val value: Int,
            val minValue: Int,
            val maxValue: Int,
            val step: Int,
            val steps: Int
        )
    }

    sealed class Effect: UiEffect
}
