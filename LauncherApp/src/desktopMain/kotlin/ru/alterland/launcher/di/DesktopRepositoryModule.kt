package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.repository.DownloadRepositoryImpl
import ru.alterland.launcher.data.repository.LaunchRepositoryImpl
import ru.alterland.launcher.domain.repository.DownloadRepository
import ru.alterland.launcher.domain.repository.LaunchRepository

internal val desktopRepositoryModule = module {
    singleOf<LaunchRepository>(::LaunchRepositoryImpl)
    single<DownloadRepository> { DownloadRepositoryImpl(dispatcherIo = get(named(DISPATCHER_IO))) }
    singleOf<LaunchRepository>(::LaunchRepositoryImpl)
}
