package ru.alterland.launcher.ui.screen.main

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import ru.alterland.launcher.ui.screen.main.serverinfo.ServerInfoScreen
import ru.alterland.launcher.ui.screen.main.container.DashboardScreen

sealed class MainScreenProvider :ScreenProvider {
    data object Dashboard: MainScreenProvider()
    data object ServerInfo: MainScreenProvider()
}

val mainScreenModule = screenModule {
    register<MainScreenProvider.Dashboard> { DashboardScreen() }
    register<MainScreenProvider.ServerInfo> { ServerInfoScreen() }
}
