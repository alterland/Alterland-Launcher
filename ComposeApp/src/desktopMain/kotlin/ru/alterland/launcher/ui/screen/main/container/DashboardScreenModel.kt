package ru.alterland.launcher.ui.screen.main.container

import ru.alterland.launchercore.Launcher
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alterland.launcher.AppConfig
import ru.alterland.launcher.data.source.local.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.launchSafe
import ru.alterland.launchercore.domain.model.ServerProfile

class DashboardScreenModel(
    private val userRepository: UserRepository,
    private val launcher: Launcher,
    private val localStorage: LocalStorage
) : BaseScreenModel<DashboardContract.Event, DashboardContract.State, DashboardContract.Effect>(
    initialState = DashboardContract.State()
) {

    override fun handleEvent(event: DashboardContract.Event) {
        when(event) {
            is DashboardContract.Event.OnSignOutClicked -> signOut()
        }
    }

    init {
        subscribeToCookies()
        subscribeToServers()
        getUser()
    }

    private fun getUser() = screenModelScope.launchSafe(onError = ::onError, onComplete = { setState { copy() } }) {
        val user = userRepository.getUser()
        setState { copy(nickName = user.nickname) }
    }

    private fun signOut() = screenModelScope.launchSafe(::onError) {
        userRepository.signOut()
    }

    private fun subscribeToCookies() {
        screenModelScope.launch {
            localStorage.cookiesFlow.map { map ->
                map[AppConfig.apiBaseUrl] ?: listOf()
            }.collect { list ->
                if (list.find { cookie -> cookie.name == "access_token" } == null) {
                    setEffect { DashboardContract.Effect.OnNavigateToAuth }
                }
            }
        }
    }

    private fun subscribeToServers() {
        launcher.servers.onEach { servers ->
            val menuItems = servers.map { server: ServerProfile ->
                MenuItem(
                    name = server.title,
                    serverStatus = server.pong.serverStatus,
                    ping = server.pong.ping,
                    max = server.pong.max,
                    online = server.pong.online,
                    favicon = server.pong.favicon
                )
            }
            setState { copy(menuItems = menuItems) }
        }.launchIn(screenModelScope)
    }
}
