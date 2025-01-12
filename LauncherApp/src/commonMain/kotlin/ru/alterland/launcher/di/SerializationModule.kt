package ru.alterland.launcher.di

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val serializationModule = module {
    singleOf(::JSON)
}

@OptIn(ExperimentalSerializationApi::class)
internal val JSON = Json {
    isLenient = true
    ignoreUnknownKeys = true
    prettyPrint = true
    explicitNulls = false
}
