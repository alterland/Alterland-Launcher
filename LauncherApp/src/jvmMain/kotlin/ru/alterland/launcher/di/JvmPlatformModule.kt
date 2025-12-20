package ru.alterland.launcher.di

import org.koin.dsl.module
import ru.alterland.launcher.PlatformConfiguration

internal val jvmPlatformModule = module {
    single { PlatformConfiguration() }
}
