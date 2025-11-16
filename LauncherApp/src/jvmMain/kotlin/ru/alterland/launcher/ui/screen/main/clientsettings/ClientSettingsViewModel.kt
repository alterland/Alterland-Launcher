package ru.alterland.launcher.ui.screen.main.clientsettings

import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.MinecraftSettingsRepository
import ru.alterland.launcher.ui.base.BaseViewModel
import kotlin.math.roundToInt

class ClientSettingsViewModel(
    private val minecraftSettingsRepository: MinecraftSettingsRepository,
    private val payload: ClientSettingsPayload
) : BaseViewModel<ClientSettingsContract.State, ClientSettingsContract.Effect, ClientSettingsContract.Action>() {

    override val container = container<ClientSettingsContract.State, ClientSettingsContract.Effect>(ClientSettingsContract.State()) {
        getSettings()
    }

    override fun dispatch(action: ClientSettingsContract.Action) {
        when(action) {
            ClientSettingsContract.Action.OnLaunchAfterUpdateClicked -> handleOnLaunchAfterUpdateClicked()
            ClientSettingsContract.Action.OnLaunchFullscreenClicked -> handleOnLaunchFullscreenClicked()
            ClientSettingsContract.Action.OnAutoConnectClicked -> handleOnAutoConnectClicked()
            is ClientSettingsContract.Action.OnRamSliderValueChange -> handleOnRamSliderValueChange(action.value)
            ClientSettingsContract.Action.OnRamSliderValueChangeFinished -> handleOnRamSliderValueChangeFinished()
        }
    }

    private fun getSettings() = intent {
        viewModelScopeErrorHandled.launch {
            val settings = minecraftSettingsRepository.getSettings(payload.id)
            reduce { state.copy(settings = settings) }
        }
    }

    private fun handleOnLaunchAfterUpdateClicked() = intent {
        viewModelScopeErrorHandled.launch {
            val updatedSettings = state.settings?.let { settings ->
                settings.copy(launchAfterUpdate = !settings.launchAfterUpdate)
            } ?: return@launch
            minecraftSettingsRepository.saveSettings(payload.id, updatedSettings)
            getSettings()
        }
    }

    private fun handleOnLaunchFullscreenClicked() = intent {
        viewModelScopeErrorHandled.launch {
            val updatedSettings = state.settings?.let { settings ->
                settings.copy(launchFullscreen = !settings.launchFullscreen)
            } ?: return@launch
            minecraftSettingsRepository.saveSettings(payload.id, updatedSettings)
            getSettings()
        }
    }

    private fun handleOnAutoConnectClicked() = intent {
        viewModelScopeErrorHandled.launch {
            val updatedSettings = state.settings?.let { settings ->
                settings.copy(autoConnect = !settings.autoConnect)
            } ?: return@launch
            minecraftSettingsRepository.saveSettings(payload.id, updatedSettings)
            getSettings()
        }
    }

    private fun handleOnRamSliderValueChange(value: Float) = intent {
//        val slider = state.ramSlider
//        val snappedValue = snapToRamStep(value, slider)
//        reduce { state.copy(ramSlider = slider.copy(value = snappedValue)) }
    }

    private fun handleOnRamSliderValueChangeFinished() = intent {

    }

    private fun snapToRamStep(value: Float, slider: ClientSettingsContract.State.Slider): Int {
        val clamped = value.coerceIn(slider.minValue.toFloat(), slider.maxValue.toFloat())
        val stepsFromMin = ((clamped - slider.minValue) / slider.step).roundToInt()
        return slider.minValue + stepsFromMin * slider.step
    }
}
