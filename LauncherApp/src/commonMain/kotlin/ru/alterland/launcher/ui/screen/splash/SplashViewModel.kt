package ru.alterland.launcher.ui.screen.splash

import kotlinx.coroutines.delay
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseViewModel

class SplashViewModel(
    private val localStorage: LocalStorage
) : BaseViewModel<SplashContract.State, SplashContract.Effect, SplashContract.Action>() {

    override val container = container<SplashContract.State, SplashContract.Effect>(SplashContract.State()) {
        subscribeToAccessToken()
    }

    override fun dispatch(action: SplashContract.Action) {}

    private fun subscribeToAccessToken() = intent {
        localStorage.accessToken.collect { token ->
            delay(700)
            val effect = if (token.isNullOrEmpty()) {
                SplashContract.Effect.NavigateToAuth
            } else {
                SplashContract.Effect.NavigateToMain
            }
            postSideEffect(effect)
        }
    }
}
