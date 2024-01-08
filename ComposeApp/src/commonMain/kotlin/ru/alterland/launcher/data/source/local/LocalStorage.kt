package ru.alterland.launcher.data.source.local

import kotlinx.coroutines.flow.MutableSharedFlow

expect class LocalStorage {
    val settingsFlow: MutableSharedFlow<Map<String, String>>
    val cookiesFlow: MutableSharedFlow<Map<String, List<PersistentCookie>>>
    fun getSettings(): MutableMap<String, String>
    fun storeSetting(key: String, value: Any)
    fun getString(key: String): String?
    fun getBoolean(key: String): Boolean?
    fun getDouble(key: String): Double?
    suspend fun storeCookie(domain: String, cookie: PersistentCookie)
    fun getAllCookies(domain: String): List<PersistentCookie>?
}
