package ru.alterland.launcher.di

import org.koin.dsl.module
import ru.alterland.launcher.PlatformConfiguration

internal val desktopPlatformModule = module {
    single { PlatformConfiguration() }
}
