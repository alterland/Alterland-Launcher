package ru.alterland.launcher.ui.screen.auth.sign_in

import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState

class SignInContract {

    sealed class Action : UiAction {
        data class OnLoginInput(val data: String): Action()
        data class OnPasswordInput(val data: String): Action()
        data object OnRememberMeChecked: Action()

        data object OnSignInClicked: Action()
        data object OnVkSignInClicked: Action()
        data object OnGoogleSignInClicked: Action()
        data object OnNavigateToRecovery: Action()
        data object OnNavigateToSignUp: Action()
    }

    data class State(
        val login: String = "",
        val password: String = "",
        val remember: Boolean = false,
        val emailErrors: String? = null,
        val passwordErrors: String? = null,
        val signInProgress: Boolean = false,
        val vkSignInProgress: Boolean = false,
        val googleSignInProgress: Boolean = false,
    ) : UiState

    sealed class Effect: UiEffect {
        data object ShowToastSocialsSignInNotYetDone: Effect()
        data object NavigateToRecovery: Effect()
        data object NavigateToSignUp: Effect()
    }
}
