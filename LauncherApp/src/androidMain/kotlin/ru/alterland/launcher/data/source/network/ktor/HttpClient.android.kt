package ru.alterland.launcher.data.source.network.ktor

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

internal actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)

    engine {
        config {
            retryOnConnectionFailure(true)
        }
    }
}
