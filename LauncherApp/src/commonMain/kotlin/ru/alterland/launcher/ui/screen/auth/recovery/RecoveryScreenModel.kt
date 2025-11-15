package ru.alterland.launcher.ui.screen.auth.recovery

import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class RecoveryScreenModel(
    private val userRepository: UserRepository
): BaseViewModel<RecoveryContract.State, RecoveryContract.Effect, RecoveryContract.Action>() {

    override val container = container<RecoveryContract.State, RecoveryContract.Effect>(RecoveryContract.State())

    override fun dispatch(action: RecoveryContract.Action) {
        when(action) {
            is RecoveryContract.Action.OnEmailInput -> handleOnEmailInput(action.data)
            is RecoveryContract.Action.OnResetPasswordClick -> handleOnResetPasswordClick()
            RecoveryContract.Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnEmailInput(data: String) = intent {
        reduce { state.copy(email = data) }
    }

    private fun handleOnResetPasswordClick() = intent {
        viewModelScopeErrorHandled.launch {
            runCatching {
                reduce { state.copy(sendCodeProgress = true) }
                userRepository.resetPassword(state.email)
                reduce { state.copy(sendCodeProgress = false) }
            }.onFailure { e ->
                errorRepository.addError(e)
                reduce { state.copy(sendCodeProgress = false) }
            }
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(RecoveryContract.Effect.NavigateBack)
    }
}
