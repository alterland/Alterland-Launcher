package ru.alterland.launcher.ui.screen

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import ru.alterland.launcher.ui.screen.auth.container.AuthContainerScreen
import ru.alterland.launcher.ui.screen.main.container.DashboardScreen

sealed class ContainerScreenProvider: ScreenProvider {
    data object Auth: ContainerScreenProvider()
    data object Dashboard: ContainerScreenProvider()
}

val containerScreenModule = screenModule {
    register<ContainerScreenProvider.Auth> { AuthContainerScreen() }
    register<ContainerScreenProvider.Dashboard> { DashboardScreen() }
}
