package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class ClientSettingsContract {

    sealed class Event : UiEvent {
        data object OnUseRecommendedRamValueClick: Event()
        data class OnRamSliderValueSelected(val value: Float): Event()

        data object OnLaunchAfterUpdateClick : Event()
        data object OnLaunchFullScreenClick : Event()
        data object OnAutoConnectClick : Event()
        data class OnApplyResolution(val width: Int, val height: Int) : Event()
        data object OnDefaultDirectoryClick : Event()

        data class OnWidthInput(val width: String): Event()
        data class OnHeightInput(val height: String): Event()

        data class OnDirectoryBrowsed(val directory: String): Event()
    }

    data class State(
        val directory: String = "",
        val isDefaultDirectory: Boolean = false,

        val width: Int = 0,
        val height: Int = 0,

        val ramSettings: RamSettings? = null,
        val useRecommendedRamValue: Boolean = false,
        val launchAfterUpdate: Boolean = false,
        var launchFullScreen: Boolean = false,
        val autoConnect: Boolean = false,
        val screenResolution: Boolean = false,
    ): UiState

    sealed class Effect: UiEffect() {

    }
}
