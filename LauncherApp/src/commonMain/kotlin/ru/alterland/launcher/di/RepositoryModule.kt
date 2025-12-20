package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.repository.*
import ru.alterland.launcher.data.source.local.LocalStorageImpl
import ru.alterland.launcher.domain.repository.*

internal val repositoryModule = module {
    singleOf<ErrorRepository>(::ErrorRepositoryImpl)
    single<LocalStorage> {
        LocalStorageImpl(
            applicationIoScope = get(named(APPLICATION_IO_SCOPE)),
            platformConfiguration = get(),
            json = get(),
            fileSystem = get()
        )
    }
    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            localStorage = get()
        )
    }
    single<MinecraftServerRepository> { MinecraftServerRepositoryImpl() }
    single<ServerProfilesRepository> {
        ServerProfilesRepositoryImpl(
            fileSystem = get(),
            serverProfilesApi = get(),
            dispatcherDefault = get(named(DISPATCHER_DEFAULT)),
            dispatcherIo = get(named(DISPATCHER_IO)),
            platformConfiguration = get(),
            json = get()
        )
    }
    single<ClientProfilesRepository> {
        ClientProfilesRepositoryImpl(
            fileSystem = get(),
            clientProfilesApi = get(),
            dispatcherDefault = get(named(DISPATCHER_DEFAULT)),
            dispatcherIo = get(named(DISPATCHER_IO)),
            platformConfiguration = get(),
            json = get()
        )
    }
    single<SkinRepository> {
        SkinRepositoryImpl(
            dispatcherIo = get(named(DISPATCHER_IO))
        )
    }
}
