package ru.alterland.launcher.ui.screen.auth.recovery

import cafe.adriel.voyager.core.model.screenModelScope
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.util.extentions.launchSafe

class RecoveryScreenModel(
    private val userRepository: UserRepository
): BaseScreenModel<RecoveryContract.Event, RecoveryContract.State, RecoveryContract.Effect>(
    initialState = RecoveryContract.State()
) {
    override fun onEvent(event: RecoveryContract.Event) {
        when(event) {
            is RecoveryContract.Event.OnEmailInput -> setState { copy(email = event.data) }
            is RecoveryContract.Event.OnResetPasswordClicked -> resetPassword()
        }
    }

    private fun resetPassword() = screenModelScope.launchSafe(
        onError = ::onError,
        onComplete = { setState { copy(sendCodeProgress = false) } }
    ) {
        setState { copy(sendCodeProgress = true) }
        userRepository.resetPassword(state.value.email)
    }
}
