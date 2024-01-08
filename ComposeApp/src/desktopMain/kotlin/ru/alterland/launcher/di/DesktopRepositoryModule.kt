package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.source.local.LocalStorage
import ru.alterland.launchercore.Launcher

internal val desktopRepositoryModule = module {
    single { LocalStorage(get(named(APPLICATION_IO_SCOPE)), get(named(DISPATCHER_IO))) }
    singleOf(::Launcher)
}
