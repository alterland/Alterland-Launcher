package ru.alterland.launcher.ui.screen.main.servers

import cafe.adriel.voyager.core.model.screenModelScope
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.launchSafe

class ServersScreenModel(
    private val userRepository: UserRepository,
    private val serverProfilesRepository: ServerProfilesRepository
): BaseScreenModel<ServersContract.Event, ServersContract.State, ServersContract.Effect>(
    initialState = ServersContract.State()
) {

    private var servers: List<ServerProfile> = listOf()

    init {
        getUser()
        getServers()
    }

    override fun onEvent(event: ServersContract.Event) {
        when(event) {
            is ServersContract.Event.OnServerSelected -> handleServerSelected(event.page)
        }
    }

    private fun getUser() = screenModelScope.launchSafe({}) {
        val user = userRepository.getUser()
        setState { copy(userStrength = user.role?.strength ?: User.Role.DEFAULT_STRENGTH) }
    }

    private fun getServers() = screenModelScope.launchSafe(::onError) {
        servers = serverProfilesRepository.getServerProfiles(force = false)
        servers.firstOrNull()?.let {
            setState {
                copy(
                    serversCount = servers.size,
                    currentServerProfile = it
                )
            }
        }
    }

    private fun handleServerSelected(page: Int) = screenModelScope.launchSafe({
        onError(it)
    }) {
        servers.getOrNull(page)?.let { profile ->
            val currentServerProfile = servers[page]
            setState {
                copy(
                    currentServerProfile = currentServerProfile,
                    currentClientProfile = currentServerProfile.clientProfile
                )
            }
        }
    }
}
