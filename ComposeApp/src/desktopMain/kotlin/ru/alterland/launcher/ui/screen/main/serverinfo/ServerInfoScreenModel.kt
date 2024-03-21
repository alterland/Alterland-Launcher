package ru.alterland.launcher.ui.screen.main.serverinfo

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe
import ru.alterland.launchercore.Launcher
import ru.alterland.launchercore.domain.model.Feature
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.Player
import ru.alterland.launchercore.domain.model.ServerProfile

class ServerInfoScreenModel(
    private val launcher: Launcher,
    private val userRepository: UserRepository
): BaseScreenModel<ServerInfoContract.Event, ServerInfoContract.State, ServerInfoContract.Effect>(
    initialState = ServerInfoContract.State()
) {

    init {
        initSubscribes()
    }

    override fun handleEvent(event: ServerInfoContract.Event) {
        when(event) {
            is ServerInfoContract.Event.OnPlayClicked -> handlePlayClick(event.profile)
        }
    }

    private fun initSubscribes() {
        launcher.servers.onEach {
            setState { copy(servers = it) }
        }.handleErrors(::onError).launchIn(screenModelScope)

        launcher.clients.onEach {
            setState { copy(clients = it) }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun handlePlayClick(profile: ServerProfile) = screenModelScope.launchSafe(::onError) {
        val user = userRepository.getUser()
        val options = Options(
            serverProfile = profile,
            player = Player(
                id = user.id,
                accessToken = user.accessToken,
                nickname = user.nickname
            ),
            features = mapOf(Feature.HAS_CUSTOM_RESOLUTION to false)
        )
        launcher.play(options)
    }
}
