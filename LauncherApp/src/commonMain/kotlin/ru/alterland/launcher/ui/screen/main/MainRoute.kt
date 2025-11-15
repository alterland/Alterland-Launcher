package ru.alterland.launcher.ui.screen.main

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface MainRoute : NavKey {

    @Serializable
    data object Servers : MainRoute
}

val mainRouteConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(MainRoute.Servers::class, MainRoute.Servers.serializer())
        }
    }
}
