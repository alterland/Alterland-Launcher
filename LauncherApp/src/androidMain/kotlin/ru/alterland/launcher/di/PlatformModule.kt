package ru.alterland.launcher.di

import android.content.Context
import org.koin.dsl.module
import ru.alterland.launcher.PlatformConfiguration

internal fun Context.platformModule() = module {
    single { PlatformConfiguration(androidContext = this@platformModule) }
}
