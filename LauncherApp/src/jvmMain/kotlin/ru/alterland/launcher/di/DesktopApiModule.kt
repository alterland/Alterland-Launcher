package ru.alterland.launcher.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.source.network.DownloadApi

internal val desktopApiModule = module {
    single { DownloadApi(dispatcherIo = get(named(DISPATCHER_IO))) }
}
