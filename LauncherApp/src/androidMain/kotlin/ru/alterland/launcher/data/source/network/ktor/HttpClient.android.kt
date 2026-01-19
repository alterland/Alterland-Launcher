package ru.alterland.launcher.data.source.network.ktor

import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun platformHttpClient(
    config: HttpClientConfig<*>.() -> Unit
) = HttpClient(OkHttp) {
    config(this)
}
