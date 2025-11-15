package ru.alterland.launcher.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.repository.ClientFilesRepositoryImpl
import ru.alterland.launcher.data.repository.ClientSettingsRepositoryImpl
import ru.alterland.launcher.data.repository.LaunchRepositoryImpl
import ru.alterland.launcher.domain.repository.ClientFilesRepository
import ru.alterland.launcher.domain.repository.ClientSettingsRepository
import ru.alterland.launcher.domain.repository.LaunchRepository

internal val desktopRepositoryModule = module {
    single<ClientFilesRepository> {
        ClientFilesRepositoryImpl(
            clientProfilesRepository = get(),
            fileSystem = get(),
            platformConfiguration = get(),
            downloadApi = get(),
            applicationIoScope = get(named(APPLICATION_IO_SCOPE)),
            json = get(),
            launchRepository = get()
        )
    }
    single<LaunchRepository> {
        LaunchRepositoryImpl(
            fileSystem = get(),
            clientProfilesRepository = get(),
            platformConfiguration = get(),
            dispatcherMain = get(named(DISPATCHER_MAIN))
        )
    }
    single<ClientSettingsRepository> {
        ClientSettingsRepositoryImpl(
            platformConfiguration = get(),
            fileSystem = get(),
            localStorage = get()
        )
    }
}
