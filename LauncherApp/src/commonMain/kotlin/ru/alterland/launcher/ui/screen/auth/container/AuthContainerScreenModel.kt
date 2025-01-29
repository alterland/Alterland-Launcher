package ru.alterland.launcher.ui.screen.auth.container

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors

class AuthContainerScreenModel(
    private val localStorage: LocalStorage
): BaseScreenModel<AuthContainerContract.Event, AuthContainerContract.State, AuthContainerContract.Effect>(
    initialState = AuthContainerContract.State()
) {

    init {
        subscribeToAccessToken()
        subscribeToErrors()
    }

    override fun onEvent(event: AuthContainerContract.Event) {
        when(event) {
            is AuthContainerContract.Event.OnMessageClose -> onMessageClose(event.id)
        }
    }

    private fun subscribeToAccessToken() {
        localStorage.accessToken.onEach {
            if (!it.isNullOrEmpty()) {
                errorRepository.clearErrors()
                setEffect { AuthContainerContract.Effect.OnNavigateToDashboard }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun subscribeToErrors() {
        errorRepository.errors.onEach { errors ->
            setState { copy(errors = errors) }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun onMessageClose(id: String) = screenModelScope.launch {
        errorRepository.removeError(id)
    }
}
