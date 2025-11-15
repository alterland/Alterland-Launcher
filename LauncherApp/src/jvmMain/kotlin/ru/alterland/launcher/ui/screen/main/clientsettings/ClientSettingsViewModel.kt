package ru.alterland.launcher.ui.screen.main.clientsettings

import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.ClientSettingsRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class ClientSettingsViewModel(
    private val clientSettingsRepository: ClientSettingsRepository,
    private val payload: ClientSettingsPayload
) : BaseViewModel<ClientSettingsContract.State, ClientSettingsContract.Effect, ClientSettingsContract.Action>() {

    override val container = container<ClientSettingsContract.State, ClientSettingsContract.Effect>(ClientSettingsContract.State())

    override fun dispatch(action: ClientSettingsContract.Action) {

    }
}
