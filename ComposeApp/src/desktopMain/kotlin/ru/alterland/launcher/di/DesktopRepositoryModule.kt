package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.alterland.launchercore.Launcher

internal val desktopRepositoryModule = module {
    singleOf(::Launcher)
}
