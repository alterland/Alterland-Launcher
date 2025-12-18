package ru.alterland.launcher.di

import org.koin.dsl.module
import ru.alterland.launcher.PlatformConfiguration

internal fun platformModule() = module {
    single { PlatformConfiguration() }
}
