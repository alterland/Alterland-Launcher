package ru.alterland.launcher.ui.screen.auth.container

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseViewModel
import ru.alterland.launcher.ui.mapper.toUi

class AuthContainerViewModel(
    private val localStorage: LocalStorage
): BaseViewModel<AuthContainerContract.State, AuthContainerContract.Effect, AuthContainerContract.Action>() {

    override val container = container<AuthContainerContract.State, AuthContainerContract.Effect>(AuthContainerContract.State()) {
        subscribeToAccessToken()
        subscribeToErrors()
    }

    override fun dispatch(action: AuthContainerContract.Action) {
        when(action) {
            is AuthContainerContract.Action.OnMessageClose -> handleOnMessageClose(action.id)
        }
    }

    private fun subscribeToAccessToken() = intent {
        localStorage.accessToken.onEach {
            if (!it.isNullOrEmpty()) {
                reduce { state.copy(errors = emptyList()) }
                postSideEffect(AuthContainerContract.Effect.NavigateToMain)
            }
        }.launchIn(viewModelScopeErrorHandled)
    }

    private fun subscribeToErrors() = intent {
        errorRepository.errors.onEach { errors ->
            reduce { state.copy(errors = errors.map { it.toUi() }) }
        }.launchIn(viewModelScopeErrorHandled)
    }

    private fun handleOnMessageClose(id: String) = viewModelScopeErrorHandled.launch {
        errorRepository.removeError(id)
    }
}
