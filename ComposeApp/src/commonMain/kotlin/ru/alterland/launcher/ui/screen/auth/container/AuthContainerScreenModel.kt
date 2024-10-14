package ru.alterland.launcher.ui.screen.auth.container

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alterland.launcher.AppConfig
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors

class AuthContainerScreenModel(
    private val localStorage: LocalStorage
): BaseScreenModel<AuthContainerContract.Event, AuthContainerContract.State, AuthContainerContract.Effect>(
    initialState = AuthContainerContract.State()
) {

    init {
        subscribeToCookies()
        subscribeToErrors()
    }

    override fun handleEvent(event: AuthContainerContract.Event) {
        when(event) {
            is AuthContainerContract.Event.OnMessageClose -> onMessageClose(event.id)
        }
    }

    private fun subscribeToCookies() {
        localStorage.cookiesFlow.map { cookies ->
            cookies[AppConfig.apiBaseUrl] ?: listOf()
        }.onEach { cookies ->
            if (cookies.find { cookie -> cookie.name == ACCESS_TOKEN_COOKIE_NAME } != null) {
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

    companion object {
        private const val ACCESS_TOKEN_COOKIE_NAME = "access_token"
    }
}
