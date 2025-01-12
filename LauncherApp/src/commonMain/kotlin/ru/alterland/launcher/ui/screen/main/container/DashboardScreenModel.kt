package ru.alterland.launcher.ui.screen.main.container

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alterland.launcher.data.source.local.LocalStoreFields
import ru.alterland.launcher.domain.model.AppEvent
import ru.alterland.launcher.domain.model.MinecraftServerStatus
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.domain.repository.*
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.ui.screen.main.container.components.MiniServerItem
import ru.alterland.launcher.util.base.Resource
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe

class DashboardScreenModel(
    private val serverProfilesRepository: ServerProfilesRepository,
    private val userRepository: UserRepository,
    private val minecraftServerRepository: MinecraftServerRepository,
    private val appEventRepository: AppEventRepository,
    private val localStorage: LocalStorage
) : BaseScreenModel<DashboardContract.Event, DashboardContract.State, DashboardContract.Effect>(
    initialState = DashboardContract.State()
) {

    private var pingServerJob: Job? = null

    init {
        subscribeToErrors()
        subscribeToStore()
        subscribeToServersUpdates()
        reload()
    }

    override fun onEvent(event: DashboardContract.Event) {
        when(event) {
            is DashboardContract.Event.OnMessageClose -> onMessageClose(event.id)
            is DashboardContract.Event.OnSignOutClicked -> signOut()
            DashboardContract.Event.OnReload -> reload()
        }
    }

    private fun reload() {
        getUser()
        getServerProfiles()
    }

    private fun getUser() = screenModelScope.launchSafe({
        setState { copy(user = Resource.Error(it)) }
    }) {
        setState { copy(user = Resource.Loading()) }
        val user = userRepository.getUser()
        setState { copy(user = Resource.Content(user)) }
    }

    private fun signOut() = screenModelScope.launchSafe(::onError) {
        userRepository.signOut()
    }

    private fun getServerProfiles() = screenModelScope.launchSafe({
        setState { copy(servers = Resource.Error(it)) }
    }) {
        setState { copy(servers = Resource.Loading()) }
        val serverProfiles = serverProfilesRepository.getServerProfiles(force = true)
        setState {
            copy(
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
    }

    private fun pingServers(serverProfiles: List<ServerProfile>) {
        pingServerJob?.cancel()
        pingServerJob = screenModelScope.launchSafe({}) {
            serverProfiles.map { serverProfile ->
                async {
                    val serverStatus = try {
                        minecraftServerRepository.ping(serverProfile.ip, serverProfile.port)
                    } catch (_: Exception) {
                        MinecraftServerStatus.Offline
                    }
                    setState {
                        val menuItemsMutable = miniServerItems.toMutableList()
                        val menuItemIndex = menuItemsMutable.indexOfFirst { it.id == serverProfile.id }
                        if (menuItemIndex != -1) {
                            val updatedMenuItem = menuItemsMutable[menuItemIndex].copy(serverStatus = serverStatus)
                            menuItemsMutable[menuItemIndex] = updatedMenuItem
                        }
                        copy(miniServerItems = menuItemsMutable)
                    }
                }
            }.awaitAll()
            delay(PING_DELAY)
            pingServers(serverProfiles)
        }
    }

    private fun subscribeToStore() {
        localStorage.storeFlow.onEach { store ->
            if (store[LocalStoreFields.ACCESS_TOKEN] == null) {
                errorRepository.clearErrors()
                setEffect { DashboardContract.Effect.OnNavigateToAuth }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun subscribeToErrors() {
        errorRepository.errors.onEach { errors ->
            setState { copy(errors = errors) }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun subscribeToServersUpdates() {
        appEventRepository.events
            .filterIsInstance<AppEvent.UpdateServerProfiles>()
            .onEach { getServerProfiles() }
            .handleErrors(::onError)
            .launchIn(screenModelScope)
    }

    private fun onMessageClose(id: String) = screenModelScope.launch {
        errorRepository.removeError(id)
    }

    companion object {
        private const val PING_DELAY = 5000L
    }
}
