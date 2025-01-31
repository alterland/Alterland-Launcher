package ru.alterland.launcher.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alterland.launcher.data.repository.*
import ru.alterland.launcher.data.source.local.LocalStorageImpl
import ru.alterland.launcher.domain.repository.*

internal val repositoryModule = module {
    singleOf<ErrorRepository>(::ErrorRepositoryImpl)
    single<AppEventRepository> { AppEventRepositoryImpl(scope = get(named(APPLICATION_SCOPE))) }
    single<LocalStorage> {
        LocalStorageImpl(
            fileSystem = get(),
            applicationIoScope = get(named(APPLICATION_IO_SCOPE)),
            platformConfiguration = get()
        )
    }
    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            localStorage = get()
        )
    }
    single<MinecraftServerRepository> {
        MinecraftServerRepositoryImpl(
            dispatcherIo = get(named(DISPATCHER_IO)),
            json = get()
        )
    }
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
