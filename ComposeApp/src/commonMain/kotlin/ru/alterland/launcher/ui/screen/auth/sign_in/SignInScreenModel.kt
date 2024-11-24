package ru.alterland.launcher.ui.screen.auth.sign_in

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.data.source.local.LocalStoreFields
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.handleErrors
import ru.alterland.launcher.util.extentions.launchSafe

class SignInScreenModel(
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage
): BaseScreenModel<SignInContract.Event, SignInContract.State, SignInContract.Effect>(
    initialState = SignInContract.State()
) {

    override fun onEvent(event: SignInContract.Event) {
        when(event) {
            is SignInContract.Event.OnLoginInput -> setState { copy(login = event.data) }
            is SignInContract.Event.OnPasswordInput -> setState { copy(password = event.data) }
            is SignInContract.Event.OnRememberMeChecked -> switchRememberMe()

            is SignInContract.Event.OnSignInClicked -> signIn()
            is SignInContract.Event.OnVkSignInClicked -> vkSignIn()
            is SignInContract.Event.OnGoogleSignInClicked -> googleSignIn()
        }
    }

    init {
        localStorage.settingsFlow.onEach {
            val remember = it[LocalStoreFields.REMEMBER].toBoolean()
            when {
                remember != state.value.remember ->
                    setState { copy(remember = remember) }
            }
        }.handleErrors(::onError).launchIn(screenModelScope)
    }

    private fun switchRememberMe() = screenModelScope.launchSafe(::onError) {
        val value = localStorage.getBoolean(LocalStoreFields.REMEMBER) ?: false
        localStorage.storeSetting(LocalStoreFields.REMEMBER, !value)
    }

    private fun signIn() = screenModelScope.launchSafe(
        onError = ::onError,
        onComplete = { setState { copy(signInProgress = false) } }
    ) {
        setState { copy(signInProgress = true) }

        userRepository.signIn(
            login = state.value.login,
            password = state.value.password
        )
    }

    private fun vkSignIn() = setEffect { SignInContract.Effect.ShowToastSocialsSignInNotYetDone }

    private fun googleSignIn() = setEffect { SignInContract.Effect.ShowToastSocialsSignInNotYetDone }
}
