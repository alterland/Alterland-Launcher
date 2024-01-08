package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.UserApi

internal val apiModule = module {
    singleOf(::UserApi)
}
