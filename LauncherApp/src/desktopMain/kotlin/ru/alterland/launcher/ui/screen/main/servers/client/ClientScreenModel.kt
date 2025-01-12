package ru.alterland.launcher.ui.screen.main.servers.client

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alterland.launcher.data.source.local.LocalStoreFields
import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.repository.*
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe

class ClientScreenModel(
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage,
    private val clientProfilesRepository: ClientProfilesRepository,
    private val downloadRepository: DownloadRepository,
    private val launchRepository: LaunchRepository,
    private val payload: ClientPayload
): BaseScreenModel<ClientContract.Event, ClientContract.State, ClientContract.Effect>(
    initialState = ClientContract.State()
) {
    private var clientProfile: ClientProfile? = null

    init {
        updateClientProfile()
        subscribeToClientProfiles()
    }

    override fun onEvent(event: ClientContract.Event) {
        when(event) {
            is ClientContract.Event.OnPlayClicked -> handlePlayClick()
        }
    }

    private fun updateClientProfile() = screenModelScope.launchSafe(::onError) {
        clientProfilesRepository.updateClientProfile(id = payload.id)
    }

    private fun handlePlayClick() = screenModelScope.launchSafe({
        //setState { copy(status = clientProfile.copy(status = ClientStatus.UpdateError(0))) }
        onError(it)
    }) {
        clientProfile?.let {
            val user = userRepository.getUser(force = false)
            val accessToken = localStorage.getString(LocalStoreFields.ACCESS_TOKEN) ?: ""

            downloadRepository.update(it)
//
//            val options = LaunchOptions(
//                clientProfile = it,
//                player = Player(
//                    id = user.id,
//                    accessToken = accessToken,
//                    nickname = user.nickname
//                ),
//                features = mapOf(Feature.HAS_CUSTOM_RESOLUTION to false)
//            )
//            launchRepository.launch(options)
        }
    }

    private fun subscribeToClientProfiles() {
        clientProfilesRepository.clientProfiles.onEach { clientProfiles ->
            clientProfiles.firstOrNull { payload.id == it.id }?.let {
                clientProfile = it
                setState { copy(status = it.status) }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }
}
