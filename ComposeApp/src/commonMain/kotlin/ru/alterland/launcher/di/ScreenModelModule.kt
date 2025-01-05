package ru.alterland.launcher.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.alterland.launcher.ui.screen.auth.container.AuthContainerScreenModel
import ru.alterland.launcher.ui.screen.auth.recovery.RecoveryScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInScreenModel
import ru.alterland.launcher.ui.screen.auth.sign_up.SignUpScreenModel
import ru.alterland.launcher.ui.screen.main.container.DashboardScreenModel
import ru.alterland.launcher.ui.screen.main.editserver.EditServerScreenModel
import ru.alterland.launcher.ui.screen.main.servers.ServersScreenModel

internal val screenModelModule = module {
    factoryOf(::AuthContainerScreenModel)
    factoryOf(::SignInScreenModel)
    factoryOf(::SignUpScreenModel)
    factoryOf(::RecoveryScreenModel)

    factoryOf(::DashboardScreenModel)
    factoryOf(::ServersScreenModel)
    factoryOf(::EditServerScreenModel)
}
