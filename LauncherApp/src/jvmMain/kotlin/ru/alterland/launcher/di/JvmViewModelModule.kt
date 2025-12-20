package ru.alterland.launcher.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.main.client.ClientViewModel
import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsViewModel

internal val jvmViewModelModule = module {
    viewModelOf(::ClientSettingsViewModel)
    viewModelOf(::ClientViewModel)
}
