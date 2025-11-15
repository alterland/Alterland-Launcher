package ru.alterland.launcher.ui.screen.main.servers

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsPayload
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.server.ServerPayload

@Serializable
sealed interface ServerRoute : NavKey {

    @Serializable
    data class Server(val payload: ServerPayload) : ServerRoute

    @Serializable
    data class EditServer(val payload: EditServerPayload) : ServerRoute

    @Serializable
    data class ClientSettings(val payload: ClientSettingsPayload) : ServerRoute
}

val serverRouteConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ServerRoute.Server::class, ServerRoute.Server.serializer())
            subclass(ServerRoute.EditServer::class, ServerRoute.EditServer.serializer())
            subclass(ServerRoute.ClientSettings::class, ServerRoute.ClientSettings.serializer())
        }
    }
}
