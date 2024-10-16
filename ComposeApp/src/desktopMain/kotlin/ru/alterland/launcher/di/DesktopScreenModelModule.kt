package ru.alterland.launcher.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsScreenModel
import ru.alterland.launcher.ui.screen.main.container.DashboardScreenModel
import ru.alterland.launcher.ui.screen.main.serverinfo.ServerInfoScreenModel

internal val desktopScreenModelModule = module {
    factoryOf(::DashboardScreenModel)
    factoryOf(::ServerInfoScreenModel)
    factoryOf(::ClientSettingsScreenModel)
}
