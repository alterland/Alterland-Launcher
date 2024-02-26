package ru.alterland.launcher.data.source.network.ktor

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.date.*
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.data.source.local.PersistentCookie

internal class CustomCookiesStorage(
    private val localStorage: LocalStorage
): CookiesStorage {

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val pc = PersistentCookie(
            name = cookie.name,
            value = cookie.value,
            maxAge = cookie.maxAge,
            expires = cookie.expires?.timestamp,
            domain = cookie.domain,
            path = cookie.path,
            secure = cookie.secure,
            httpOnly = cookie.httpOnly,
            extensions = cookie.extensions
        )
        localStorage.storeCookie(requestUrl.host, pc)
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> = localStorage.getAllCookies(requestUrl.host).map {
        it.toCookie()
    }

    private fun PersistentCookie.toCookie(): Cookie {
        return Cookie(
            name = this.name,
            value = this.value,
            encoding = CookieEncoding.URI_ENCODING,
            maxAge = this.maxAge ?: 0,
            expires = GMTDate(this.expires),
            domain = this.domain,
            path = this.path,
            secure = this.secure,
            httpOnly = this.httpOnly,
            extensions = this.extensions ?: emptyMap()
        )
    }
}
