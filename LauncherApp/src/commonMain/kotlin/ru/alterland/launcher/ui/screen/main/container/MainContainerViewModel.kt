package ru.alterland.launcher.ui.screen.main.container

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.MinecraftServerRepository
import ru.alterland.launcher.domain.repository.ServerProfilesRepository
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel
import ru.alterland.launcher.ui.mapper.toUi
import ru.alterland.launcher.ui.model.ServerProfileWithStatus
import ru.alterland.launcher.util.base.Resource

class MainContainerViewModel(
    private val serverProfilesRepository: ServerProfilesRepository,
    private val userRepository: UserRepository,
    private val minecraftServerRepository: MinecraftServerRepository,
    private val localStorage: LocalStorage
) : BaseViewModel<MainContainerContract.State, MainContainerContract.Effect, MainContainerContract.Action>() {

    override val container = container<MainContainerContract.State, MainContainerContract.Effect>(MainContainerContract.State())

    private var pingServerJobs: List<Job>? = null

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
        try {
            userRepository.signOut()
        } finally {
            postSideEffect(MainContainerContract.Effect.NavigateToAuth)
        }
    }

    private fun getServerProfiles() = intent {
        reduce { state.copy(serverProfiles = Resource.Loading()) }
        runCatching {
            serverProfilesRepository.getServerProfiles()
        }.onFailure { throwable ->
            reduce { state.copy(serverProfiles = Resource.Error(throwable)) }
            errorRepository.addError(throwable)
        }
    }

    private fun pingServers() = intent {
        pingServerJobs?.forEach { it.cancel() }
        val serverProfiles = (state.serverProfiles as? Resource.Content)?.data
        if (serverProfiles.isNullOrEmpty()) return@intent
        pingServerJobs = serverProfiles.map { item ->
            viewModelScopeErrorHandled.launch {
                while (isActive) {
                    val serverStatus = runCatching {
                        minecraftServerRepository.ping(item.serverProfile.ip, item.serverProfile.port)
                    }.getOrElse { MinecraftServerStatus.Offline }
                    reduce {
                        val updated = (state.serverProfiles as? Resource.Content)?.data?.let { data ->
                            Resource.Content(data = data.map {
                                if (it.serverProfile.id == item.serverProfile.id) {
                                    ServerProfileWithStatus(
                                        serverProfile = item.serverProfile,
                                        status = serverStatus
                                    )
                                } else it
                            })
                        } ?: state.serverProfiles
                        state.copy(serverProfiles = updated)
                    }
                    delay(PING_DELAY)
                }
            }
        }
    }

    private fun subscribeToAccessToken() = intent {
        localStorage.accessToken.collect { token ->
            if (token.isNullOrEmpty()) {
                errorRepository.clearErrors()
                postSideEffect(MainContainerContract.Effect.NavigateToAuth)
            }
        }
    }

    private fun subscribeToErrors() = intent {
        errorRepository.errors.collect { errors ->
            reduce { state.copy(errors = errors.map { it.toUi() }) }
        }
    }

    private fun subscribeToServerProfiles() = intent {
        serverProfilesRepository.serverProfiles.collect { serverProfiles ->
            reduce { state.copy(serverProfiles = Resource.Content(serverProfiles.map {
                ServerProfileWithStatus(it) }))
            }
            pingServers()
        }
    }

    private fun onMessageClose(id: String) = viewModelScopeErrorHandled.launch {
        errorRepository.removeError(id)
    }

    companion object {
        private const val PING_DELAY = 10000L
    }
}
