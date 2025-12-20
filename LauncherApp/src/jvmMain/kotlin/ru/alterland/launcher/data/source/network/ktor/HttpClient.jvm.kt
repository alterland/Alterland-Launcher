package ru.alterland.launcher.data.source.network.ktor

import io.ktor.client.*
import io.ktor.client.engine.cio.*

internal actual fun platformHttpClient(
    config: HttpClientConfig<*>.() -> Unit
) = HttpClient(CIO) {
    config()
}
