package ru.alterland.launcher.ui.screen.auth.sign_up

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SignUpScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SignUpScreenModel>()
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        SignUp(
            state = state,
            setEvent = { e -> screenModel.setEvent(e) },
            navigateBack = { navigator.pop() }
        )
    }
}