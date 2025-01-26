package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel

class ClientSettingsScreenModel(
    private val localStorage: LocalStorage,
    private val platformConfiguration: PlatformConfiguration
) : BaseScreenModel<ClientSettingsContract.Event, ClientSettingsContract.State, ClientSettingsContract.Effect>(
    initialState = ClientSettingsContract.State(
        directory = "/Users/aviator737/alterland",
        width = SCREEN_WIDTH_DEFAULT,
        height = SCREEN_HEIGHT_DEFAULT
    )
) {

    private val recommendedCustomRam = RamSettings.CustomRamSettings(TEMP_RECOMMENDED_MEM, 16, 1024f, 16384f)

    private val defaultDirectory = platformConfiguration.rootDir

    init {
        initRamSettings()
    }

    override fun onEvent(event: ClientSettingsContract.Event) {
        when(event) {
            is ClientSettingsContract.Event.OnRamSliderValueSelected -> handleRamSliderValueSelected(event.value)
            ClientSettingsContract.Event.OnUseRecommendedRamValueClick -> handleOnUseRecommendedRamValueClick()
            is ClientSettingsContract.Event.OnLaunchAfterUpdate -> handleOnLaunchAfterUpdate()
            is ClientSettingsContract.Event.OnLaunchFullScreen -> handleOnLaunchFullScreen()
            is ClientSettingsContract.Event.OnAutoConnect -> handleOnAutoConnect()
            is ClientSettingsContract.Event.OnApplyResolution -> handleApplyResolution()
            is ClientSettingsContract.Event.OnDefaultDirectory -> handleDefaultDirectory()

            is ClientSettingsContract.Event.OnWidthInput -> handleWidthInput(event.width)
            is ClientSettingsContract.Event.OnHeightInput -> handleHeightInput(event.height)

            is ClientSettingsContract.Event.OnDirectoryBrowsed -> handleDirectoryBrowsed(event.directory)
        }
    }

    private fun handleDirectoryBrowsed(directory: String) {
        setState { copy(directory = directory) }
    }

    private fun handleWidthInput(width: String) {
        val w = try { width.toInt() } catch (_: Exception) { SCREEN_WIDTH_DEFAULT }
        setState { copy(width = w) }
    }

    private fun handleHeightInput(height: String) {
        val h = try { height.toInt() } catch (_: Exception) { SCREEN_HEIGHT_DEFAULT }
        setState { copy(width = h) }
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
        //setState { copy(defaultDirectory = !defaultDirectory)}
    }

    private fun initRamSettings() {
        setState { copy(ramSettings = recommendedCustomRam) }
    }

    companion object {
        private const val SCREEN_WIDTH_DEFAULT = 600
        private const val SCREEN_HEIGHT_DEFAULT = 400

        private const val TEMP_RECOMMENDED_MEM = 6445.0f
    }
}
