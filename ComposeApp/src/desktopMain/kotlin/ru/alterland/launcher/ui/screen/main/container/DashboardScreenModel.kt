package ru.alterland.launcher.ui.screen.main.container

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alterland.launcher.data.source.local.LocalStoreFields
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe
import ru.alterland.launchercore.Launcher
import ru.alterland.launchercore.domain.model.ServerProfile

class DashboardScreenModel(
    private val userRepository: UserRepository,
    private val launcher: Launcher,
    private val localStorage: LocalStorage
) : BaseScreenModel<DashboardContract.Event, DashboardContract.State, DashboardContract.Effect>(
    initialState = DashboardContract.State()
) {

    init {
        subscribeToErrors()
        subscribeToStore()
        subscribeToServers()
        subscribeToClientServiceStatus()
        getUser()
    }

    override fun onEvent(event: DashboardContract.Event) {
        when(event) {
            is DashboardContract.Event.OnMessageClose -> onMessageClose(event.id)
            is DashboardContract.Event.OnSignOutClicked -> signOut()
        }
    }

    private fun getUser() = screenModelScope.launchSafe(onError = ::onError, onComplete = { setState { copy() } }) {
        val user = userRepository.getUser()
        setState { copy(nickName = user.nickname) }
    }

    private fun signOut() = screenModelScope.launchSafe(::onError) {
        userRepository.signOut()
    }

    private fun subscribeToStore() {
        localStorage.storeFlow.onEach { store ->
            if (store[LocalStoreFields.ACCESS_TOKEN] == null) {
                errorRepository.clearErrors()
                setEffect { DashboardContract.Effect.OnNavigateToAuth }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun subscribeToClientServiceStatus() {
        launcher.isOffline.onEach {
            setState { copy(isClientServiceOffline = it) }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun subscribeToErrors() {
        errorRepository.errors.onEach { errors ->
            setState { copy(errors = errors) }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun subscribeToServers() {
        launcher.servers.onEach { servers ->
            val menuItems = servers.map { server: ServerProfile ->
                MenuItem(
                    name = server.title,
                    serverStatus = server.serverStatus
                )
            }
            setState { copy(menuItems = menuItems) }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun onMessageClose(id: String) = screenModelScope.launch {
        errorRepository.removeError(id)
    }
}
