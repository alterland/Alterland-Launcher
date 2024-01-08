package ru.alterland.launcher.data.source.local

import kotlinx.coroutines.flow.MutableSharedFlow

actual class LocalStorage {

    //TODO call preferences

    actual val settingsFlow: MutableSharedFlow<Map<String, String>> = TODO("Not yet implemented")
    actual val cookiesFlow: MutableSharedFlow<Map<String, List<PersistentCookie>>> = TODO("Not yet implemented")

    actual fun getSettings(): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    actual fun storeSetting(key: String, value: Any) {
    }

    actual fun getString(key: String): String? {
        TODO("Not yet implemented")
    }

    actual fun getBoolean(key: String): Boolean? {
        TODO("Not yet implemented")
    }

    actual fun getDouble(key: String): Double? {
        TODO("Not yet implemented")
    }

    actual suspend fun storeCookie(
        domain: String,
        cookie: PersistentCookie
    ) {
    }

    actual fun getAllCookies(domain: String): List<PersistentCookie>? {
        TODO("Not yet implemented")
    }

}