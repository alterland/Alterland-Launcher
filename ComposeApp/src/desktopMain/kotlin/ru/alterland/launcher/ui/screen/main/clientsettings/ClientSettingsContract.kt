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
        data object OnDefaultDirectory : Event()

        data class OnWidthInput(val data: String): Event()
        data class OnHeightInput(val data: String): Event()

        data class OnPathChange(val path: String) : Event()
        data class OnInputRamSettings(val data: String): Event()
    }

    data class State(
        val ramSettings: RamSettings? = null,
        val useRecommendedRamValue: Boolean = true,
        val launchAfterUpdate: Boolean = false,
        val autoConnect: Boolean = false,

        var launchFullScreen: Boolean = true,
        val width: String = "",
        val height: String = "",
        val screenSize: String = "",

        val directoryPath: String = "",
        val defaultDirectory: Boolean = true,

        val inputRamSettings: String = "",

    ): UiState

    sealed class Effect: UiEffect() {

    }
}
