package ru.alterland.launcher.ui.screen.auth.sign_up

import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.util.base.Resource

class SignUpContract {
    sealed class Action : UiAction {
        data class OnNickInput(val data: String): Action()
        data class OnEmailInput(val data: String): Action()
        data class OnPasswordInput(val data: String): Action()

        data object OnSignUpClicked: Action()
        data object OnVkSignUpClicked: Action()
        data object OnGoogleSignUpClicked: Action()
        data object OnNavigateBack: Action()
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

    sealed class Effect: UiEffect {
        data object ShowToastSocialsSignInNotYetDone: Effect()
        data object NavigateBack: Effect()
    }
}
