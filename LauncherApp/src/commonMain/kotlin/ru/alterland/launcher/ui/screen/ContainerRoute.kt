package ru.alterland.launcher.ui.screen

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface ContainerRoute: NavKey {

    @Serializable
    data object Auth: ContainerRoute

    @Serializable
    data object Main: ContainerRoute
}

val containerRouteConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ContainerRoute.Auth::class, ContainerRoute.Auth.serializer())
            subclass(ContainerRoute.Main::class, ContainerRoute.Main.serializer())
        }
    }
}
