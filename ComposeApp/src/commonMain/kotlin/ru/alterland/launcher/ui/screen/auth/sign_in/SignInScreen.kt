package ru.alterland.launcher.ui.screen.auth.sign_in

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.alterland.launcher.ui.screen.auth.AuthScreenProvider

class SignInScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SignInScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val recoveryScreen = rememberScreen(AuthScreenProvider.Recovery)
        val signUpScreen = rememberScreen(AuthScreenProvider.SignUp)

        SignIn(
            state = state,
            setEvent = { e -> screenModel.setEvent(e) },
            navigateToRecovery = { navigator.push(recoveryScreen) },
            navigateToSignUp = { navigator.push(signUpScreen) }
        )
    }
}
