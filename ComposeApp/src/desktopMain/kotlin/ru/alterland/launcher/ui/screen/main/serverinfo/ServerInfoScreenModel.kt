package ru.alterland.launcher.ui.screen.main.serverinfo

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.base.AppException
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe
import ru.alterland.launchercore.Launcher
import ru.alterland.launchercore.domain.model.*

class ServerInfoScreenModel(
    private val launcher: Launcher,
    private val userRepository: UserRepository
): BaseScreenModel<ServerInfoContract.Event, ServerInfoContract.State, ServerInfoContract.Effect>(
    initialState = ServerInfoContract.State()
) {

    private var servers: List<ServerProfile> = listOf()
    private var clients: List<ClientProfile> = listOf()

    init {
        initSubscribes()
    }

    override fun onEvent(event: ServerInfoContract.Event) {
        when(event) {
            is ServerInfoContract.Event.OnServerSelected -> handleServerSelected(event.page)
            is ServerInfoContract.Event.OnPlayClicked -> handlePlayClick(event.clientProfile)
        }
    }

    private fun initSubscribes() {
        launcher.servers.onEach {
            val initServers = servers.isEmpty() && it.isNotEmpty()
            servers = it
            setState { copy(serversCount = it.size) }
            if (initServers) {
                handleServerSelected(0)
            }
        }.handleErrors(::onError).launchIn(screenModelScope)

        launcher.clients.onEach {
            clients = it
            clients.firstOrNull { client -> client.id == state.value.currentClientProfile?.id }?.let {
                setState { copy(currentClientProfile = it) }
                if (it.status is ClientStatus.UpdateError) {
                    onError(AppException.UpdateException(((it.status as ClientStatus.UpdateError).errorCount)))
                }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun handleServerSelected(page: Int) = screenModelScope.launchSafe({
        setState { copy(isFetchingClientProfile = false) }
        onError(it)
    }) {
        servers.getOrNull(page)?.let { serverProfile ->
            setState { copy(currentServerProfile = serverProfile) }
            serverProfile.clientProfile?.let { clientProfileId ->
                val clientProfile = clients.firstOrNull { it.id == clientProfileId } ?: run {
                    setState { copy(isFetchingClientProfile = true) }
                    launcher.fetchClientProfile(clientProfileId)
                }
                setState {
                    copy(
                        currentClientProfile = clientProfile,
                        isFetchingClientProfile = false
                    )
                }
            }
        }
    }

    private fun handlePlayClick(clientProfile: ClientProfile) = screenModelScope.launchSafe({
        setState { copy(currentClientProfile = clientProfile.copy(status = ClientStatus.UpdateError(0))) }
        onError(it)
    }) {
        if (clientProfile.status is ClientStatus.Updating) {
            launcher.toggleDownload(clientProfile)
        } else {
            setState { copy(currentClientProfile = clientProfile.copy(status = ClientStatus.Verification)) }
            val user = userRepository.getUser()
            val options = Options(
                clientProfile = clientProfile,
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
}
