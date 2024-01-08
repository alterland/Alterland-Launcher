package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.alterland.launcher.PlatformConfiguration

internal val platformModule = module {
    singleOf(::PlatformConfiguration)
}
