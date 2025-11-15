package ru.alterland.launcher.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.main.client.ClientViewModel
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsViewModel

internal val desktopScreenModelModule = module {
    factoryOf(::ClientSettingsViewModel)
    factoryOf(::ClientViewModel)
}
