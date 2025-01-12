package ru.alterland.launcher.ui.screen.auth

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import ru.alterland.launcher.ui.screen.auth.recovery.RecoveryScreen
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInScreen
import ru.alterland.launcher.ui.screen.auth.sign_up.SignUpScreen

sealed class AuthScreenProvider : ScreenProvider {
    data object SignIn : AuthScreenProvider()
    data object SignUp : AuthScreenProvider()
    data object Recovery : AuthScreenProvider()
}

val authScreenModule = screenModule {
    register<AuthScreenProvider.SignIn> { SignInScreen() }
    register<AuthScreenProvider.SignUp> { SignUpScreen() }
    register<AuthScreenProvider.Recovery> { RecoveryScreen() }
}
