package ru.alterland.launcher.ui.screen.main

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsScreen
import ru.alterland.launcher.ui.screen.main.container.DashboardScreen
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.editserver.EditServerScreen
import ru.alterland.launcher.ui.screen.main.server.ServerPayload
import ru.alterland.launcher.ui.screen.main.server.ServerScreen
import ru.alterland.launcher.ui.screen.main.servers.ServersScreen
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload
import ru.alterland.launcher.ui.screen.main.servers.client.ClientScreen
import ru.alterland.launcher.ui.screen.main.skins.Skins
import ru.alterland.launcher.ui.screen.main.skins.SkinsScreen

sealed class MainScreenProvider: ScreenProvider {
    data object Dashboard: MainScreenProvider()
    data object Servers: MainScreenProvider()
    data class Server(val payload: ServerPayload): MainScreenProvider()
    data class ClientSettings(val payload: ClientPayload): MainScreenProvider()
    data class Client(val payload: ClientPayload): MainScreenProvider()
    data class EditServer(val payload: EditServerPayload): MainScreenProvider()
    data object Skins: MainScreenProvider()
}

val mainScreenModule = screenModule {
    register<MainScreenProvider.Dashboard> { DashboardScreen() }
    register<MainScreenProvider.Servers> { ServersScreen() }
    register<MainScreenProvider.Server> { provider -> ServerScreen(payload = provider.payload) }
    register<MainScreenProvider.ClientSettings> { provider -> ClientSettingsScreen(payload = provider.payload) }
    register<MainScreenProvider.Client> { provider -> ClientScreen(payload = provider.payload) }
    register<MainScreenProvider.EditServer> { provider -> EditServerScreen(payload = provider.payload) }
    register<MainScreenProvider.Skins> { SkinsScreen() }
}
