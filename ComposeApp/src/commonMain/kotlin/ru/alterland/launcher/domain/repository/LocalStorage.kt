package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.alterland.launcher.data.source.local.PersistentCookie

interface LocalStorage {
    val settingsFlow: Flow<Map<String, String>>
    val cookiesFlow: Flow<Map<String, List<PersistentCookie>>>
    suspend fun getSettings(): Map<String, String>
    suspend fun storeSetting(key: String, value: Any)
    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?
    suspend fun getDouble(key: String): Double?
    suspend fun storeCookie(domain: String, cookie: PersistentCookie)
    suspend fun removeCookie(domain: String, cookieName: String)
    suspend fun getAllCookies(domain: String): List<PersistentCookie>
    suspend fun removeAllCookies()
}
