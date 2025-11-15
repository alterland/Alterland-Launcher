package ru.alterland.launcher.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.auth.container.AuthContainerViewModel
import ru.alterland.launcher.ui.screen.auth.recovery.RecoveryScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_up.SignUpScreenModel
import ru.alterland.launcher.ui.screen.main.container.MainContainerViewModel
import ru.alterland.launcher.ui.screen.main.editserver.EditServerViewModel
import ru.alterland.launcher.ui.screen.main.server.ServerViewModel
import ru.alterland.launcher.ui.screen.main.servers.ServersViewModel

internal val viewModelModule = module {
    factoryOf(::AuthContainerViewModel)
    factoryOf(::SignInScreenModel)
    factoryOf(::SignUpScreenModel)
    factoryOf(::RecoveryScreenModel)

    factoryOf(::MainContainerViewModel)
    factoryOf(::ServersViewModel)
    factoryOf(::ServerViewModel)
    factoryOf(::EditServerViewModel)
}
