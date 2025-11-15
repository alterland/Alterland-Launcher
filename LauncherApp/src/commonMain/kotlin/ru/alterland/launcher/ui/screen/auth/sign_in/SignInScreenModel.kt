package ru.alterland.launcher.ui.screen.auth.sign_in

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class SignInScreenModel(
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage
): BaseViewModel<SignInContract.State, SignInContract.Effect, SignInContract.Action>() {

    override val container = container<SignInContract.State, SignInContract.Effect>(SignInContract.State()) {
        subscribeToRememberMe()
    }

    override fun dispatch(action: SignInContract.Action) {
        when(action) {
            is SignInContract.Action.OnLoginInput -> handleOnLoginInput(action.data)
            is SignInContract.Action.OnPasswordInput -> handleOnPasswordInput(action.data)
            is SignInContract.Action.OnRememberMeChecked -> handleOnRememberMeChecked()

            is SignInContract.Action.OnSignInClicked -> handleOnSignInClicked()
            is SignInContract.Action.OnVkSignInClicked -> handleOnVkSignInClicked()
            is SignInContract.Action.OnGoogleSignInClicked -> handleOnGoogleSignInClicked()
            SignInContract.Action.OnNavigateToRecovery -> handleOnNavigateToRecovery()
            SignInContract.Action.OnNavigateToSignUp -> handleOnNavigateToSignUp()
        }
    }

    private fun subscribeToRememberMe() = intent {
        localStorage.storeFlow.onEach {
            val remember = it?.rememberMe == true
            when {
                remember != state.remember -> reduce { state.copy(remember = remember) }
            }
        }.launchIn(viewModelScopeErrorHandled)
    }

    private fun handleOnLoginInput(data: String) = intent {
        reduce { state.copy(login = data) }
    }

    private fun handleOnPasswordInput(data: String) = intent {
        reduce { state.copy(password = data) }
    }

    private fun handleOnRememberMeChecked() = intent {
        viewModelScopeErrorHandled.launch {
            localStorage.update { it?.copy(rememberMe = !state.remember) }
        }
    }

    private fun handleOnSignInClicked() = intent {
        viewModelScopeErrorHandled.launch {
            runCatching {
                reduce { state.copy(signInProgress = true) }
                userRepository.signIn(
                    login = state.login,
                    password = state.password
                )
                reduce { state.copy(signInProgress = false) }
            }.onFailure { e ->
                reduce { state.copy(signInProgress = false) }
                errorRepository.addError(e)
            }
        }
    }

    private fun handleOnNavigateToRecovery() = intent {
        postSideEffect(SignInContract.Effect.NavigateToRecovery)
    }

    private fun handleOnNavigateToSignUp() = intent {
        postSideEffect(SignInContract.Effect.NavigateToSignUp)
    }

    private fun handleOnVkSignInClicked() = intent {
        postSideEffect(SignInContract.Effect.ShowToastSocialsSignInNotYetDone)
    }

    private fun handleOnGoogleSignInClicked() = intent {
        postSideEffect(SignInContract.Effect.ShowToastSocialsSignInNotYetDone)
    }
}
