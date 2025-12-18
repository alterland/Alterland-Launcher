package ru.alterland.launcher.ui.screen.main.servers

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class ServersViewModel(
    private val userRepository: UserRepository,
    private val serverProfilesRepository: ServerProfilesRepository
): BaseViewModel<ServersContract.State, ServersContract.Effect, ServersContract.Action>() {

    override val container = container<ServersContract.State, ServersContract.Effect>(ServersContract.State()) {
        getUser()
        subscribeToServerProfiles()
    }

    override fun dispatch(action: ServersContract.Action) {}

    private fun getUser() = intent {
        val user = userRepository.getUser()
        reduce { state.copy(userStrength = user.role?.strength ?: User.Role.DEFAULT_STRENGTH) }
    }

    private fun subscribeToServerProfiles() = intent {
        serverProfilesRepository.serverProfiles.onEach { serverProfiles ->
            reduce { state.copy(serverProfiles = serverProfiles) }
        }.launchIn(viewModelScopeErrorHandled)
    }
}
