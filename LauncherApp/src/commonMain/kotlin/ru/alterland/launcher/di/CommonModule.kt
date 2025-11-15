package ru.alterland.launcher.di

val commonModule = listOf(
    fileSystemModule,
    serializationModule,
    coroutineModule,
    ktorModule,
    apiModule,
    repositoryModule,
    viewModelModule
)
