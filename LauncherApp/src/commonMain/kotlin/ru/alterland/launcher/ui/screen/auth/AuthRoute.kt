package ru.alterland.launcher.ui.screen.auth

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface AuthRoute : NavKey {

    @Serializable
    data object SignIn : AuthRoute

    @Serializable
    data object SignUp : AuthRoute

    @Serializable
    data object Recovery : AuthRoute
}

val authRouteConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(AuthRoute.SignIn::class, AuthRoute.SignIn.serializer())
            subclass(AuthRoute.SignUp::class, AuthRoute.SignUp.serializer())
            subclass(AuthRoute.Recovery::class, AuthRoute.Recovery.serializer())
        }
    }
}
