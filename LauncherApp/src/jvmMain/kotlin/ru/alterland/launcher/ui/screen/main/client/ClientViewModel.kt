package ru.alterland.launcher.ui.screen.main.client

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.Feature
import ru.alterland.launcher.domain.model.clientprofile.Player
import ru.alterland.launcher.domain.repository.ClientFilesRepository
import ru.alterland.launcher.domain.repository.ClientProfilesRepository
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class ClientViewModel(
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage,
    private val clientProfilesRepository: ClientProfilesRepository,
    private val clientFilesRepository: ClientFilesRepository,
    private val payload: ClientPayload
): BaseViewModel<ClientContract.State, ClientContract.Effect, ClientContract.Action>() {

    override val container = container<ClientContract.State, ClientContract.Effect>(ClientContract.State())

    private var user: User? = null
    private var clientProfile: ClientProfile? = null

    init {
        subscribeToUser()
        updateClientProfile()
        subscribeToClientProfiles()
    }

    override fun dispatch(action: ClientContract.Action) {
        when(action) {
            is ClientContract.Action.OnPlayClicked -> handlePlayClick()
        }
    }

    private fun subscribeToUser() = intent {
        userRepository.user.collect { resource ->
            user = resource.getOrNull()
        }
    }

    private fun updateClientProfile() = viewModelScopeErrorHandled.launch {
        clientProfilesRepository.updateClientProfile(id = payload.id)
    }

    private fun handlePlayClick() = intent {
        user?.let { user ->
            clientProfile?.let {
                //setState { copy(status = clientProfile.copy(status = ClientStatus.UpdateError(0))) }
                clientFilesRepository.updateAndLaunch(
                    clientProfile = it,
                    player = Player(
                        id = user.id,
                        accessToken = localStorage.accessToken.value.orEmpty(),
                        nickname = user.nickname
                    ),
                    features = mapOf(Feature.HAS_CUSTOM_RESOLUTION to false)
                )
            }
        }
    }

    private fun subscribeToClientProfiles() = intent {
        clientProfilesRepository.clientProfiles.onEach { clientProfiles ->
            clientProfiles.firstOrNull { payload.id == it.id }?.let {
                clientProfile = it
                reduce { state.copy(status = it.status) }
            }
        }.launchIn(viewModelScopeErrorHandled)
    }
}
