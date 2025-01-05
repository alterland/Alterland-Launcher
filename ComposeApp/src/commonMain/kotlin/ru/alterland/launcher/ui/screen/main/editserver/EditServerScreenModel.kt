package ru.alterland.launcher.ui.screen.main.editserver

import cafe.adriel.voyager.core.model.screenModelScope
import ru.alterland.launcher.data.repository.MinecraftServerRepositoryImpl.Companion.DEFAULT_PORT
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.base.Resource
import ru.alterland.launcher.util.extentions.launchSafe

class EditServerScreenModel(
    private val serverProfilesRepository: ServerProfilesRepository,
    private val clientProfilesRepository: ClientProfilesRepository,
    private val payload: EditServerPayload
): BaseScreenModel<EditServerContract.Event, EditServerContract.State, EditServerContract.Effect>(
    initialState = EditServerContract.State()
) {

    init {
        getServerProfile()
        getClientProfiles()
    }

    override fun onEvent(event: EditServerContract.Event) {
        when(event) {
            is EditServerContract.Event.OnTitleInput -> handleTitleInput(event.data)
            is EditServerContract.Event.OnDescriptionInput -> handleDescriptionInput(event.data)
            is EditServerContract.Event.OnIPInput -> handleIPInput(event.data)
            is EditServerContract.Event.OnPortInput -> handlePortInput(event.data)
            EditServerContract.Event.OnSaveClick -> handleSaveClick()
        }
    }

    private fun getServerProfile() = screenModelScope.launchSafe(onError = ::onError) {
        if (payload is EditServerPayload.Edit) {
            val serverProfile = serverProfilesRepository.getServerProfile(payload.serverProfileId)
            if (serverProfile != null) {
                setState { copy(serverProfile = serverProfile) }
            }
        }
    }

    private fun getClientProfiles() = screenModelScope.launchSafe(onError = {
        setState { copy(clientProfileObjects = Resource.Error(it)) }
    }) {
        setState { copy(clientProfileObjects = Resource.Loading()) }
        val clientProfiles = clientProfilesRepository.getClientProfileObjects()
        setState { copy(clientProfileObjects = Resource.Content(clientProfiles)) }
    }

    private fun saveAdd() = screenModelScope.launchSafe(onError = ::onError) {
        serverProfilesRepository.addServerProfile(state.value.serverProfile)
    }

    private fun saveEdit() = screenModelScope.launchSafe(onError = ::onError) {
        serverProfilesRepository.editServerProfile(state.value.serverProfile)
    }

    private fun handleSaveClick() = when(payload) {
        EditServerPayload.Add -> saveAdd()
        is EditServerPayload.Edit -> saveEdit()
    }

    private fun handleTitleInput(data: String) {
        setState {
            copy(serverProfile.copy(title = data))
        }
    }

    private fun handleDescriptionInput(data: String) {
        setState {
            copy(serverProfile.copy(description = data))
        }
    }

    private fun handleIPInput(data: String) {
        setState {
            copy(serverProfile.copy(ip = data))
        }
    }

    private fun handlePortInput(data: String) {
        val port = try { data.toInt() } catch (_: Exception) { DEFAULT_PORT }
        setState {
            copy(serverProfile.copy(port = port))
        }
    }
}
