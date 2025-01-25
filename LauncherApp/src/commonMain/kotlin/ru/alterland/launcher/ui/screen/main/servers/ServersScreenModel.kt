package ru.alterland.launcher.ui.screen.main.servers

import cafe.adriel.voyager.core.model.screenModelScope
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

    init {
        getUser()
        getServers()
    }

    override fun onEvent(event: ServersContract.Event) {}

    private fun getUser() = screenModelScope.launchSafe(::onError) {
        val user = userRepository.getUser()
        setState { copy(userStrength = user.role?.strength ?: User.Role.DEFAULT_STRENGTH) }
    }

    private fun getServers() = screenModelScope.launchSafe(::onError) {
        val servers = serverProfilesRepository.getServerProfiles(force = false)
        setState { copy(serverProfiles = servers) }
    }
}
