package ru.alterland.launcher.ui.screen.main.editserver

import ru.alterland.launcher.data.repository.MinecraftServerRepositoryImpl.Companion.DEFAULT_PORT
import ru.alterland.launcher.domain.model.clientprofile.ClientProfileObject
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.util.base.Resource

class EditServerContract {

    sealed class Event : UiEvent {
        data class OnTitleInput(val data: String): Event()
        data class OnDescriptionInput(val data: String): Event()
        data class OnIPInput(val data: String): Event()
        data class OnPortInput(val data: String): Event()
        data object OnSaveClick: Event()
    }

    data class State(
        val isSaveInProgress: Boolean = false,
        val serverProfile: ServerProfile = ServerProfile(
            id = "",
            title = "",
            description = "",
            ip = "",
            port = DEFAULT_PORT,
            clientProfile = ""
        ),
        val clientProfileObjects: Resource<List<ClientProfileObject>>? = null
    ): UiState

    sealed class Effect: UiEffect() {
        data object OnNavigateBack: Effect()
    }
}
