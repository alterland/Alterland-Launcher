package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInContract

class ClientSettingsScreenModel(
    private val localStorage: LocalStorage
) : BaseScreenModel<ClientSettingsContract.Event, ClientSettingsContract.State, ClientSettingsContract.Effect>(
    initialState = ClientSettingsContract.State()
) {

    private val recommendedCustomRam = RamSettings.CustomRamSettings(TEMP_RECOMMENDED_MEM, 16, 1024f, 16384f)

    init {
        initRamSettings()
    }

    override fun handleEvent(event: ClientSettingsContract.Event) {
        when(event) {
            is ClientSettingsContract.Event.OnRamSliderValueSelected -> handleRamSliderValueSelected(event.value)
            ClientSettingsContract.Event.OnUseRecommendedRamValueClick -> handleOnUseRecommendedRamValueClick()
            is ClientSettingsContract.Event.OnLaunchAfterUpdate -> handleOnLaunchAfterUpdate()
            is ClientSettingsContract.Event.OnLaunchFullScreen -> handleOnLaunchFullScreen()
            is ClientSettingsContract.Event.OnAutoConnect -> handleOnAutoConnect()
            is ClientSettingsContract.Event.OnApplyResolution -> handleApplyResolution()
            is ClientSettingsContract.Event.OnDefaultDirectory -> handleDefaultDirectory()

            is ClientSettingsContract.Event.OnWidthInput -> setState { copy(width = event.data) }
            is ClientSettingsContract.Event.OnHeightInput -> setState { copy(height = event.data) }

            is ClientSettingsContract.Event.OnPathChange -> setState { copy(directoryPath = event.path)}
            is ClientSettingsContract.Event.OnBrowseDirectory -> {} //-
        }
    }

    private fun handleRamSliderValueSelected(value: Float) {
        setState { copy(ramSettings = recommendedCustomRam.copy(value = value)) }
    }

    private fun handleOnUseRecommendedRamValueClick() {
        setState { copy(ramSettings = recommendedCustomRam, useRecommendedRamValue = !useRecommendedRamValue) }
    }

    private fun handleOnLaunchAfterUpdate() {
        setState { copy(launchAfterUpdate = !launchAfterUpdate) }
    }

    private fun handleOnLaunchFullScreen() {
        setState { copy(launchFullScreen = !launchFullScreen) }
    }

    private fun handleOnAutoConnect() {
        setState { copy(autoConnect = !autoConnect) }
    }

    private fun handleApplyResolution() {
        setState { copy(screenResolution = !screenResolution) }
    }

    private fun handleDefaultDirectory() {
        setState { copy(defaultDirectory = !defaultDirectory)}
    }

    private fun initRamSettings() {
        setState { copy(ramSettings = recommendedCustomRam) }
    }

    companion object {
        private const val TEMP_RECOMMENDED_MEM = 6445.0f
    }
}
