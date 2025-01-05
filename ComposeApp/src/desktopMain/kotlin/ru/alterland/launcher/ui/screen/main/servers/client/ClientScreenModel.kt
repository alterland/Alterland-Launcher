package ru.alterland.launcher.ui.screen.main.servers.client

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alterland.launcher.data.source.local.LocalStoreFields
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe
import ru.alterland.launchercore.Launcher
import ru.alterland.launchercore.domain.model.ClientProfile
import ru.alterland.launchercore.domain.model.Feature
import ru.alterland.launchercore.domain.model.Options
import ru.alterland.launchercore.domain.model.Player

class ClientScreenModel(
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage,
    private val launcher: Launcher,
    private val payload: ClientPayload
): BaseScreenModel<ClientContract.Event, ClientContract.State, ClientContract.Effect>(
    initialState = ClientContract.State()
) {
    private var clientProfile: ClientProfile? = null

    init {
        getClientProfile()
        subscribeToClientProfiles()
    }

    override fun onEvent(event: ClientContract.Event) {
        when(event) {
            is ClientContract.Event.OnPlayClicked -> handlePlayClick()
        }
    }

    private fun getClientProfile() = screenModelScope.launchSafe(::onError) {
        clientProfile = launcher.getClientProfile(id = payload.id, force = true)
    }

    private fun handlePlayClick() = screenModelScope.launchSafe({
        //setState { copy(status = clientProfile.copy(status = ClientStatus.UpdateError(0))) }
        onError(it)
    }) {
        val user = userRepository.getUser(force = false)
        val accessToken = localStorage.getString(LocalStoreFields.ACCESS_TOKEN) ?: ""

        clientProfile?.let {
            val options = Options(
                clientProfile = it,
                player = Player(
                    id = user.id,
                    accessToken = accessToken,
                    nickname = user.nickname
                ),
                features = mapOf(Feature.HAS_CUSTOM_RESOLUTION to false)
            )
            launcher.play(options)
        }
    }

    private fun subscribeToClientProfiles() {
        launcher.clientProfiles.onEach { clientProfiles ->
            clientProfiles.firstOrNull { payload.id == it.id }?.let {
                setState { copy(status = it.status) }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }
}
