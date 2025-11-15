package ru.alterland.launcher.ui.screen.main.container

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.MinecraftServerRepository
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel
import ru.alterland.launcher.ui.mapper.toUi
import ru.alterland.launcher.ui.screen.main.container.components.MiniServerItem
import ru.alterland.launcher.util.base.Resource

class MainContainerViewModel(
    private val serverProfilesRepository: ServerProfilesRepository,
    private val userRepository: UserRepository,
    private val minecraftServerRepository: MinecraftServerRepository,
    private val localStorage: LocalStorage
) : BaseViewModel<MainContainerContract.State, MainContainerContract.Effect, MainContainerContract.Action>() {

    override val container = container<MainContainerContract.State, MainContainerContract.Effect>(MainContainerContract.State())

    private var pingServerJob: Job? = null

    init {
        subscribeToErrors()
        subscribeToAccessToken()
        subscribeToServerProfiles()
        reload()
    }

    override fun dispatch(action: MainContainerContract.Action) {
        when(action) {
            is MainContainerContract.Action.OnMessageClose -> onMessageClose(action.id)
            is MainContainerContract.Action.OnSignOutClicked -> signOut()
            MainContainerContract.Action.OnReload -> reload()
            MainContainerContract.Action.OnNavigateToAddServer -> handleOnNavigateToAddServer()
        }
    }

    private fun handleOnNavigateToAddServer() = intent {

    }

    private fun reload() {
        getUser()
        getServerProfiles()
    }

    private fun getUser() = intent {
        reduce { state.copy(user = Resource.Loading()) }
        runCatching { userRepository.getUser() }
            .onSuccess { user ->
                reduce { state.copy(user = Resource.Content(user)) }
            }
            .onFailure { throwable ->
                reduce { state.copy(user = Resource.Error(throwable)) }
                errorRepository.addError(throwable)
            }
    }

    private fun signOut() = intent {
        runCatching { userRepository.signOut() }
            .onFailure { throwable -> errorRepository.addError(throwable) }
    }

    private fun getServerProfiles() = intent {
        reduce { state.copy(servers = Resource.Loading()) }
        runCatching { serverProfilesRepository.getServerProfiles() }
            .onSuccess {
                val hasServers = serverProfilesRepository.serverProfiles.value.isNotEmpty()
                reduce { state.copy(servers = Resource.Content(hasServers)) }
            }
            .onFailure { throwable ->
                reduce { state.copy(servers = Resource.Error(throwable)) }
                errorRepository.addError(throwable)
            }
    }

    private fun pingServers(serverProfiles: List<ServerProfile>) {
        pingServerJob?.cancel()
        if (serverProfiles.isEmpty()) {
            return
        }
        pingServerJob = intent {
            viewModelScopeErrorHandled.launch {
                while (isActive) {
                    val statuses = serverProfiles.map { serverProfile ->
                        async {
                            val serverStatus = runCatching {
                                minecraftServerRepository.ping(serverProfile.ip, serverProfile.port)
                            }.getOrElse { MinecraftServerStatus.Offline }
                            serverProfile.id to serverStatus
                        }
                    }.awaitAll().toMap()
                    reduce {
                        state.copy(
                            miniServerItems = state.miniServerItems.map { item ->
                                statuses[item.id]?.let { status -> item.copy(serverStatus = status) } ?: item
                            }
                        )
                    }
                    delay(PING_DELAY)
                }
            }
        }
    }

    private fun subscribeToAccessToken() {
        localStorage.accessToken.onEach { token ->
            if (token.isNullOrEmpty()) {
                intent {
                    try {
                        errorRepository.clearErrors()
                    } catch (throwable: Throwable) {
                        errorRepository.addError(throwable)
                    }
                    postSideEffect(MainContainerContract.Effect.NavigateToAuth)
                }
            }
        }.launchIn(viewModelScopeErrorHandled)
    }

    private fun subscribeToErrors() {
        errorRepository.errors.onEach { errors ->
            intent {
                reduce { state.copy(errors = errors.map { it.toUi() }) }
            }
        }.launchIn(viewModelScopeErrorHandled)
    }

    private fun subscribeToServerProfiles() = intent {
        serverProfilesRepository.serverProfiles.onEach { serverProfiles ->
            reduce {
                state.copy(
                    miniServerItems = serverProfiles.map {
                        MiniServerItem(
                            id = it.id,
                            name = it.title,
                            serverStatus = MinecraftServerStatus.Polling
                        )
                    },
                    servers = Resource.Content(serverProfiles.isNotEmpty()),
                )
            }
            pingServers(serverProfiles)
        }.launchIn(viewModelScopeErrorHandled)
    }

    private fun onMessageClose(id: String) = viewModelScopeErrorHandled.launch {
        errorRepository.removeError(id)
    }

    companion object {
        private const val PING_DELAY = 5000L
    }
}
