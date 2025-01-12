package ru.alterland.launcher.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsScreenModel
import ru.alterland.launcher.ui.screen.main.servers.client.ClientScreenModel

internal val desktopScreenModelModule = module {
    factoryOf(::ClientSettingsScreenModel)
    factoryOf(::ClientScreenModel)
}
