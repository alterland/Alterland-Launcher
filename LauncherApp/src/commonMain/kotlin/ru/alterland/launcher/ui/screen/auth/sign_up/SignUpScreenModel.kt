package ru.alterland.launcher.ui.screen.auth.sign_up

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel
import ru.alterland.launcher.util.base.Resource

class SignUpScreenModel(
    private val userRepository: UserRepository
): BaseViewModel<SignUpContract.State, SignUpContract.Effect, SignUpContract.Action>() {

    override val container = container<SignUpContract.State, SignUpContract.Effect>(SignUpContract.State()) {
        searchNick()
    }

    private var searchNickJob: Job? = null

    override fun dispatch(action: SignUpContract.Action) {
        when(action) {
            is SignUpContract.Action.OnNickInput -> handleOnNickInput(action.data)
            is SignUpContract.Action.OnEmailInput -> handleOnEmailInput(action.data)
            is SignUpContract.Action.OnPasswordInput -> handleOnPasswordInput(action.data)

            is SignUpContract.Action.OnSignUpClicked -> handleOnSignUpClicked()
            is SignUpContract.Action.OnVkSignUpClicked -> vkSignUp()
            is SignUpContract.Action.OnGoogleSignUpClicked -> googleSignUp()
            SignUpContract.Action.OnNavigateBack -> handleOnNavigateBack()
        }
    }

    private fun searchNick() = intent {
        searchNickJob?.cancel()
        searchNickJob = viewModelScopeErrorHandled.launch {
            delay(1300)
            val nickName = state.nickName
            if (nickName.isNotEmpty()) {
                runCatching {
                    if (nickName.isNotEmpty()) {
                        reduce { state.copy(checkNickQuery = Resource.Loading()) }
                        val result = userRepository.checkNick(nickName)
                        reduce { state.copy(checkNickQuery = Resource.Content(result)) }
                    } else {
                        reduce { state.copy(checkNickQuery = null) }
                    }
                }
            }
        }
    }

    private fun handleOnNickInput(text: String) = intent {
        reduce { state.copy(nickName = text) }
        searchNick()
    }

    private fun handleOnEmailInput(data: String) = intent {
        reduce { state.copy(email = data) }
    }

    private fun handleOnPasswordInput(data: String) = intent {
        reduce { state.copy(password = data) }
    }

    private fun handleOnSignUpClicked() = intent {
        viewModelScopeErrorHandled.launch {
            runCatching {
                errorRepository.clearErrors()
                reduce { state.copy(signUpProgress = true) }
                userRepository.signUp(
                    nickname = state.nickName,
                    email = state.email,
                    password = state.password
                )
                reduce { state.copy(signUpProgress = false) }
            }.onFailure { e ->
                reduce { state.copy(signUpProgress = false) }
                errorRepository.addError(e)
            }
        }
    }

    private fun vkSignUp() = intent {
        postSideEffect(SignUpContract.Effect.ShowToastSocialsSignInNotYetDone)
    }

    private fun googleSignUp() = intent {
        postSideEffect(SignUpContract.Effect.ShowToastSocialsSignInNotYetDone)
    }

    private fun handleOnNavigateBack() = intent {
        postSideEffect(SignUpContract.Effect.NavigateBack)
    }
}
