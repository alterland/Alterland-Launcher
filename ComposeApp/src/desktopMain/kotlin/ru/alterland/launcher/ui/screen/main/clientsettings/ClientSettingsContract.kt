package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class ClientSettingsContract {

    sealed class Event : UiEvent {
        data object OnUseRecommendedRamValueClick: Event()
        data class OnRamSliderValueSelected(val value: Float): Event()

        data object OnLaunchAfterUpdate : Event()
        data object OnLaunchFullScreen : Event()
        data object OnAutoConnect : Event()
        data class OnApplyResolution(val width: Int, val height: Int) : Event()
    }

    data class State(
        val ramSettings: RamSettings? = null,
        val useRecommendedRamValue: Boolean = false,
        val launchAfterUpdate: Boolean = false,
        var launchFullScreen: Boolean = false,
        val autoConnect: Boolean = false,
        val screenResolution: Boolean = false

    ): UiState

    sealed class Effect: UiEffect {

    }
}
