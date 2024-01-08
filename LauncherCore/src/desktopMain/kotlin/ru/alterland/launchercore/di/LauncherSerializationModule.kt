package ru.alterland.launchercore.di

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val launcherSerializationModule = module {
    singleOf(::JSON)
}

@OptIn(ExperimentalSerializationApi::class)
internal val JSON = Json {
    explicitNulls = false
    isLenient = true
    ignoreUnknownKeys = true
    prettyPrint = true
}
