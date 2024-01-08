package ru.alterland.launchercore.data.source.network.ktor

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

class HttpEngineFactory {
    fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = OkHttp
}
