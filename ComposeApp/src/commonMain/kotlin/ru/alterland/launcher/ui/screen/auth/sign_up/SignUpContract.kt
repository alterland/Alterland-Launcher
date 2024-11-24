package ru.alterland.launcher.ui.screen.auth.sign_up

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.util.base.Resource

class SignUpContract {
    sealed class Event : UiEvent {
        data class OnInitialLoginSet(val data: String): Event()
        data class OnNickInput(val data: String): Event()
        data class OnEmailInput(val data: String): Event()
        data class OnPasswordInput(val data: String): Event()

        data object OnSignUpClicked: Event()
        data object OnVkSignUpClicked: Event()
        data object OnGoogleSignUpClicked: Event()
    }

    data class State(
        val nickName: String = "",
        val email: String = "",
        val password: String = "",
        val checkNickQuery: Resource<Boolean?>? = null,
        val signUpProgress: Boolean = false,
        val vkSignUpProgress: Boolean = false,
        val googleSignUpProgress: Boolean = false,
    ) : UiState

    sealed class Effect: UiEffect() {
        data object ShowToastSocialsSignInNotYetDone: Effect()
    }
}
