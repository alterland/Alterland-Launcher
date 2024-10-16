package ru.alterland.launcher.ui.screen.main

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsScreen
import ru.alterland.launcher.ui.screen.main.container.DashboardScreen
import ru.alterland.launcher.ui.screen.main.serverinfo.ServerInfoScreen

sealed class MainScreenProvider: ScreenProvider {
    data object Dashboard: MainScreenProvider()
    data object ServerInfo: MainScreenProvider()
    data object ClientSettings: MainScreenProvider()
}

val mainScreenModule = screenModule {
    register<MainScreenProvider.Dashboard> { DashboardScreen() }
    register<MainScreenProvider.ServerInfo> { ServerInfoScreen() }
    register<MainScreenProvider.ClientSettings> { ClientSettingsScreen() }
}
