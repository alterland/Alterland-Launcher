package ru.alterland.launcher.data.source.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

internal actual fun platformHttpClient(
    config: HttpClientConfig<*>.() -> Unit
) = HttpClient(Darwin) {
    config()
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}
