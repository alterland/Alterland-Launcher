package ru.alterland.launcher.ui.screen.auth.sign_in

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

class SignInContract {

    sealed class Event : UiEvent {
        data class OnLoginInput(val data: String): Event()
        data class OnPasswordInput(val data: String): Event()
        data object OnRememberMeChecked: Event()

        data object OnSignInClicked: Event()
        data object OnVkSignInClicked: Event()
        data object OnGoogleSignInClicked: Event()
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
        data object OnNavigateToDashboard: Effect()
    }

}