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
        data object OnDefaultDirectory : Event()

        data class OnWidthInput(val data: String): Event()
        data class OnHeightInput(val data: String): Event()

        data class OnPathChange(val path: String) : Event()
        data object OnBrowseDirectory : Event()
    }

    data class State(
        val ramSettings: RamSettings? = null,
        val useRecommendedRamValue: Boolean = false,
        val launchAfterUpdate: Boolean = false,
        var launchFullScreen: Boolean = false,
        val autoConnect: Boolean = false,
        val screenResolution: Boolean = false,

        val width: String = "",
        val height: String = "",

        val directoryPath: String = "",
        val defaultDirectory: Boolean = false,

    ): UiState

    sealed class Effect: UiEffect {

    }
}
