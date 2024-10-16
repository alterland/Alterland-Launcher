package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel

class ClientSettingsScreenModel(
    private val localStorage: LocalStorage
) : BaseScreenModel<ClientSettingsContract.Event, ClientSettingsContract.State, ClientSettingsContract.Effect>(
    initialState = ClientSettingsContract.State()
) {

    private val recommendedCustomRam = RamSettings.CustomRamSettings(TEMP_RECOMMENDED_MEM, 32, 1024f, 16384f)

    init {
        initRamSettings()
    }

    override fun handleEvent(event: ClientSettingsContract.Event) {
        when(event) {
            is ClientSettingsContract.Event.OnRamSliderValueSelected -> handleRamSliderValueSelected(event.value)
            ClientSettingsContract.Event.OnUseRecommendedRamValueClick -> handleOnUseRecommendedRamValueClick()
        }
    }

    private fun handleRamSliderValueSelected(value: Float) {
        setState { copy(ramSettings = recommendedCustomRam.copy(value = value)) }
    }

    private fun handleOnUseRecommendedRamValueClick() {
        setState { copy(ramSettings = recommendedCustomRam, useRecommendedRamValue = !useRecommendedRamValue) }
    }

    private fun initRamSettings() {
        setState { copy(ramSettings = recommendedCustomRam) }
    }

    companion object {
        private const val TEMP_RECOMMENDED_MEM = 6144.0f
    }
}
