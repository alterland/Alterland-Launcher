package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class ClientSettingsContract {

    sealed class Event : UiEvent {
        data object OnUseRecommendedRamValueClick: Event()
        data class OnRamSliderValueSelected(val value: Float): Event()
    }

    data class State(
        val ramSettings: RamSettings? = null,
        val useRecommendedRamValue: Boolean = false
    ): UiState

    sealed class Effect: UiEffect {

    }
}
