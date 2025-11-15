package ru.alterland.launcher.ui.screen.main.editserver

import ru.alterland.launcher.data.repository.MinecraftServerRepositoryImpl.Companion.DEFAULT_PORT
import ru.alterland.launcher.domain.model.clientprofile.ClientProfileObject
import ru.alterland.launcher.domain.model.ServerProfile
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.util.base.Resource

class EditServerContract {

    sealed class Action : UiAction {
        data class OnTitleInput(val data: String): Action()
        data class OnDescriptionInput(val data: String): Action()
        data class OnIPInput(val data: String): Action()
        data class OnPortInput(val data: String): Action()
        data object OnSaveClick: Action()
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

    sealed class Effect: UiEffect {
        data object NavigateBack: Effect()
    }
}
