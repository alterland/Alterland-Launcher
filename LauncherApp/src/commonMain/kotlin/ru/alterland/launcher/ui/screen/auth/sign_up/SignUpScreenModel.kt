package ru.alterland.launcher.ui.screen.auth.sign_up

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.base.Resource
import ru.alterland.launcher.util.extentions.launchSafe

class SignUpScreenModel(
    private val userRepository: UserRepository
): BaseScreenModel<SignUpContract.Event, SignUpContract.State, SignUpContract.Effect>(
    initialState = SignUpContract.State()
) {

    private var searchNickJob: Job? = null

    override fun onEvent(event: SignUpContract.Event) {
        when(event) {
            is SignUpContract.Event.OnInitialLoginSet -> onInitialLoginSet(event.data)

            is SignUpContract.Event.OnNickInput -> onNickInput(event.data)
            is SignUpContract.Event.OnEmailInput -> setState { copy(email = event.data) }
            is SignUpContract.Event.OnPasswordInput -> setState { copy(password = event.data) }

            is SignUpContract.Event.OnSignUpClicked -> signUp()
            is SignUpContract.Event.OnVkSignUpClicked -> vkSignUp()
            is SignUpContract.Event.OnGoogleSignUpClicked -> googleSignUp()
        }
    }

    init {
        searchNick()
    }

    private fun searchNick() {
        searchNickJob?.cancel()
        searchNickJob = screenModelScope.launchSafe(onError = ::onError) {
            delay(1300)
            setState { copy(checkNickQuery = Resource.Loading()) }
            val nickName = state.value.nickName
            if (nickName.isNotEmpty()) {
                setState { copy(checkNickQuery = Resource.Loading()) }
                val result = userRepository.checkNick(nickName)
                setState { copy(checkNickQuery = Resource.Content(result)) }
            } else {
                setState { copy(checkNickQuery = null) }
            }
        }
    }

    private fun onInitialLoginSet(login: String = "", password: String = "") {
        setState { copy(email = login, password = password) }
    }

    private fun onNickInput(text: String) {
        setState { copy(nickName = text) }
        searchNick()
    }

    private fun signUp() = screenModelScope.launchSafe(
        onError = ::onError,
        onComplete = { setState { copy(signUpProgress = false) } }
    ) {
        errorRepository.clearErrors()
        setState { copy(signUpProgress = true) }

        with(state.value) {
            userRepository.signUp(nickName, email, password)
        }
    }

    private fun vkSignUp() = setEffect { SignUpContract.Effect.ShowToastSocialsSignInNotYetDone }

    private fun googleSignUp() = setEffect { SignUpContract.Effect.ShowToastSocialsSignInNotYetDone }
}
