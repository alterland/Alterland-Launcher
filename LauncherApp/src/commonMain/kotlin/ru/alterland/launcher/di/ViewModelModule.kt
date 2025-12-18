package ru.alterland.launcher.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.auth.container.AuthContainerViewModel
import ru.alterland.launcher.ui.screen.auth.recovery.RecoveryScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_up.SignUpScreenModel
import ru.alterland.launcher.ui.screen.main.container.MainContainerViewModel
import ru.alterland.launcher.ui.screen.main.editserver.EditServerViewModel
import ru.alterland.launcher.ui.screen.main.server.ServerViewModel
import ru.alterland.launcher.ui.screen.main.servers.ServersViewModel
import ru.alterland.launcher.ui.screen.main.skins.SkinsViewModel
import ru.alterland.launcher.ui.screen.splash.SplashViewModel

internal val viewModelModule = module {
    viewModelOf(::SplashViewModel)

    viewModelOf(::AuthContainerViewModel)
    viewModelOf(::SignInScreenModel)
    viewModelOf(::SignUpScreenModel)
    viewModelOf(::RecoveryScreenModel)

    viewModelOf(::MainContainerViewModel)
    viewModelOf(::ServersViewModel)
    viewModelOf(::ServerViewModel)
    viewModelOf(::EditServerViewModel)
    viewModelOf(::SkinsViewModel)
}
