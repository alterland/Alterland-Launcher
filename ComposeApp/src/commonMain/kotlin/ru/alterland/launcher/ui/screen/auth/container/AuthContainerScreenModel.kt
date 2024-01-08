package ru.alterland.launcher.ui.screen.auth.container

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alterland.launcher.AppConfig
import ru.alterland.launcher.data.source.local.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel

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

    private fun subscribeToCookies() = screenModelScope.launch {
        localStorage.cookiesFlow.map { map ->
            map[AppConfig.apiBaseUrl] ?: listOf()
        }.collect { list ->
            if (list.find { cookie -> cookie.name == "access_token" } != null) {
                errorRepository.clearErrors()
                setEffect { AuthContainerContract.Effect.OnNavigateToDashboard }
            }
        }
    }

    private fun subscribeToErrors() {
        errorRepository.errors.onEach { errors ->
            setState { copy(errors = errors) }
        }.launchIn(screenModelScope)
    }

    private fun onMessageClose(id: String) = screenModelScope.launch {
        errorRepository.removeError(id)
    }
}
