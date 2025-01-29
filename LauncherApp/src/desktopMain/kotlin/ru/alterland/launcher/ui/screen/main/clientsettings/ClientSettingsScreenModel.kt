package ru.alterland.launcher.ui.screen.main.clientsettings

import cafe.adriel.voyager.core.model.screenModelScope
import ru.alterland.launcher.domain.repository.ClientSettingsRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.launchSafe

class ClientSettingsScreenModel(
    private val clientSettingsRepository: ClientSettingsRepository
) : BaseScreenModel<ClientSettingsContract.Event, ClientSettingsContract.State, ClientSettingsContract.Effect>(
    initialState = ClientSettingsContract.State()
) {
    private val recommendedCustomRam = RamSettings.CustomRamSettings(TEMP_RECOMMENDED_MEM, 16, 1024f, 16384f)

    init {
        initSettings()
    }

    override fun onEvent(event: ClientSettingsContract.Event) {
        when(event) {
            is ClientSettingsContract.Event.OnDirectoryBrowsed -> handleDirectoryBrowsed(event.directory)
            is ClientSettingsContract.Event.OnDefaultDirectoryClick -> handleOnDefaultDirectoryClick()

            is ClientSettingsContract.Event.OnRamSliderValueSelected -> handleRamSliderValueSelected(event.value)
            ClientSettingsContract.Event.OnUseRecommendedRamValueClick -> handleOnUseRecommendedRamValueClick()
            is ClientSettingsContract.Event.OnLaunchAfterUpdateClick -> handleOnLaunchAfterUpdate()
            is ClientSettingsContract.Event.OnLaunchFullScreenClick -> handleOnLaunchFullScreen()
            is ClientSettingsContract.Event.OnAutoConnectClick -> handleOnAutoConnect()
            is ClientSettingsContract.Event.OnApplyResolution -> handleApplyResolution()

            is ClientSettingsContract.Event.OnWidthInput -> handleWidthInput(event.width)
            is ClientSettingsContract.Event.OnHeightInput -> handleHeightInput(event.height)
        }
    }

    private fun initSettings() = screenModelScope.launchSafe(::onError) {
        val currentDirectory = clientSettingsRepository.getCurrentDirectory()
        setState {
            copy(
                directory = currentDirectory,
                isDefaultDirectory = currentDirectory == clientSettingsRepository.getDefaultDirectory()
            )
        }
    }

    private fun handleDirectoryBrowsed(directory: String) = screenModelScope.launchSafe(::onError) {
        clientSettingsRepository.setCurrentDirectory(directory)
        setState {
            copy(
                directory = directory,
                isDefaultDirectory = directory == clientSettingsRepository.getDefaultDirectory()
            )
        }
    }

    private fun handleOnDefaultDirectoryClick() = screenModelScope.launchSafe(::onError) {
        val defaultDirectory = clientSettingsRepository.getDefaultDirectory()
        clientSettingsRepository.setCurrentDirectory(defaultDirectory)
        setState {
            copy(
                directory = defaultDirectory,
                isDefaultDirectory = true
            )
        }
    }

    private fun handleWidthInput(width: String) {
        setState { copy(width = width.toIntOrNull() ?: 100) }
    }

    private fun handleHeightInput(height: String) {
        setState { copy(width = height.toIntOrNull() ?: 100) }
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

    private fun initRamSettings() {
        setState { copy(ramSettings = recommendedCustomRam) }
    }

    companion object {
        private const val TEMP_RECOMMENDED_MEM = 6445.0f
    }
}
