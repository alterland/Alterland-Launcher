package ru.alterland.launcher.ui.screen.main.editserver

import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.data.repository.MinecraftServerRepositoryImpl.Companion.DEFAULT_PORT
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.ui.base.BaseViewModel
import ru.alterland.launcher.util.base.Resource

class EditServerViewModel(
    private val serverProfilesRepository: ServerProfilesRepository,
    private val clientProfilesRepository: ClientProfilesRepository,
    private val payload: EditServerPayload
): BaseViewModel<EditServerContract.State, EditServerContract.Effect, EditServerContract.Action>() {

    override val container = container<EditServerContract.State, EditServerContract.Effect>(EditServerContract.State())

    init {
        setServerProfile()
        getClientProfiles()
    }

    override fun dispatch(action: EditServerContract.Action) {
        when(action) {
            is EditServerContract.Action.OnTitleInput -> handleTitleInput(action.data)
            is EditServerContract.Action.OnDescriptionInput -> handleDescriptionInput(action.data)
            is EditServerContract.Action.OnIPInput -> handleIPInput(action.data)
            is EditServerContract.Action.OnPortInput -> handlePortInput(action.data)
            EditServerContract.Action.OnSaveClick -> handleSaveClick()
        }
    }

    private fun setServerProfile() = intent {
        if (payload.mode is EditServerMode.Edit) {
            reduce { state.copy(serverProfile = payload.mode.serverProfile) }
        }
    }

    private fun getClientProfiles() = intent {
        reduce { state.copy(clientProfileObjects = Resource.Loading()) }
        runCatching { clientProfilesRepository.getClientProfileObjects() }
            .onSuccess { clientProfiles ->
                reduce { state.copy(clientProfileObjects = Resource.Content(clientProfiles)) }
            }
            .onFailure { throwable ->
                reduce { state.copy(clientProfileObjects = Resource.Error(throwable)) }
                errorRepository.addError(throwable)
            }
    }

    private fun handleSaveClick() = intent {
        reduce { state.copy(isSaveInProgress = true) }
        runCatching {
            when(payload.mode) {
                EditServerMode.Add -> serverProfilesRepository.addServerProfile(state.serverProfile)
                is EditServerMode.Edit -> serverProfilesRepository.editServerProfile(state.serverProfile)
            }
        }.onSuccess {
            reduce { state.copy(isSaveInProgress = false) }
            postSideEffect(EditServerContract.Effect.NavigateBack)
        }.onFailure { throwable ->
            reduce { state.copy(isSaveInProgress = false) }
            errorRepository.addError(throwable)
        }
    }

    private fun handleTitleInput(data: String) = intent {
        reduce { state.copy(serverProfile = state.serverProfile.copy(title = data)) }
    }

    private fun handleDescriptionInput(data: String) = intent {
        reduce { state.copy(serverProfile = state.serverProfile.copy(description = data)) }
    }

    private fun handleIPInput(data: String) = intent {
        reduce { state.copy(serverProfile = state.serverProfile.copy(ip = data)) }
    }

    private fun handlePortInput(data: String) = intent {
        val port = data.toIntOrNull() ?: DEFAULT_PORT
        reduce { state.copy(serverProfile = state.serverProfile.copy(port = port)) }
    }
}
