package ru.alterland.launcher.ui.screen.main.clientsettings

import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.MinecraftSettingsRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class ClientSettingsViewModel(
    private val minecraftSettingsRepository: MinecraftSettingsRepository,
    private val payload: ClientSettingsPayload
) : BaseViewModel<ClientSettingsContract.State, ClientSettingsContract.Effect, ClientSettingsContract.Action>() {

    override val container = container<ClientSettingsContract.State, ClientSettingsContract.Effect>(
        initialState = ClientSettingsContract.State(
            recommendedRam = 8192
        )
    ) {
        getSettings()
    }

    override fun dispatch(action: ClientSettingsContract.Action) {
        when(action) {
            ClientSettingsContract.Action.OnLaunchAfterUpdateClicked -> handleOnLaunchAfterUpdateClicked()
            ClientSettingsContract.Action.OnLaunchFullscreenClicked -> handleOnLaunchFullscreenClicked()
            ClientSettingsContract.Action.OnAutoConnectClicked -> handleOnAutoConnectClicked()
            is ClientSettingsContract.Action.OnRamInput -> handleOnRamInput(action.value)
            ClientSettingsContract.Action.OnRamInputFinished -> getSettings()
        }
    }

    private fun getSettings() = intent {
        viewModelScopeErrorHandled.launch {
            val settings = minecraftSettingsRepository.getSettings(payload.id)
            reduce { state.copy(settings = settings, ramValue = settings.ram.toString()) }
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

    private fun handleOnRamInput(value: String) = intent {
        reduce { state.copy(ramValue = value) }
        val intValue = value.toIntOrNull() ?: return@intent
        val memory = intValue.coerceIn(MIN_MEMORY, MAX_MEMORY)
        if (memory != state.settings?.ram) {
            viewModelScopeErrorHandled.launch {
                val updatedSettings = state.settings?.copy(ram = memory) ?: return@launch
                minecraftSettingsRepository.saveSettings(payload.id, updatedSettings)
            }
        }
    }

    companion object {
        private const val MAX_MEMORY = 16384
        private const val MIN_MEMORY = 1536
    }
}
