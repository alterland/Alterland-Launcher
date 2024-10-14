package ru.alterland.launcher.data.source.local

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.source.local.LocalStoreFields.RAM
import ru.alterland.launcher.data.source.local.LocalStoreFields.REMEMBER
import ru.alterland.launcher.data.source.local.model.Store
import ru.alterland.launcher.domain.repository.LocalStorage
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists

class LocalStorageImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val applicationIoScope: CoroutineScope,
    private val platformConfiguration: PlatformConfiguration
) : LocalStorage {

    private val store: KStore<Store> = storeOf(
        file = platformConfiguration.storeDir.toPath(),
        enableCache = true
    )

    private val tempCookies: MutableStateFlow<Map<String, List<PersistentCookie>>> = MutableStateFlow(mapOf())

    override val settingsFlow: Flow<Map<String, String>> = store.updates.map { it?.settings ?: mapOf() }

    override val cookiesFlow: Flow<Map<String, List<PersistentCookie>>> =
        store.updates.map { it?.cookies ?: mapOf() }.combine(tempCookies) {
            cookies: Map<String, List<PersistentCookie>>, tempCookies: Map<String, List<PersistentCookie>> ->
            val result = cookies.toMutableMap()
            tempCookies.forEach { (domain, tempDomainCookies) ->
                val storeDomainCookies = cookies[domain]
                if (storeDomainCookies != null) {
                    val mergeCookies = storeDomainCookies.filter { storeCookie ->
                        tempDomainCookies.find { it.name == storeCookie.name } != null
                    }.toMutableList()
                    mergeCookies.addAll(tempDomainCookies)
                    result[domain] = mergeCookies
                } else {
                    result[domain] = tempDomainCookies
                }
            }
            result
        }

    init {
        createStoreIfNotExist()
    }

    override suspend fun getSettings(): Map<String, String> = store.get()?.settings ?: mapOf()

    override suspend fun storeSetting(key: String, value: Any) = withContext(dispatcherIo) {
        store.update {
            it?.settings?.let { settings ->
                val settingsMutable = settings.toMutableMap()
                settingsMutable[key] = value.toString()
                it.copy(settings = settingsMutable)
            }
        }
    }

    override suspend fun getString(key: String) = store.get()?.let { store ->
        store.settings[key]
    }

    override suspend fun getBoolean(key: String) = store.get()?.let { store ->
        store.settings[key]?.toBooleanStrictOrNull()
    }

    override suspend fun getDouble(key: String) = store.get()?.let { store ->
        store.settings[key]?.toDoubleOrNull()
    }

    override suspend fun storeCookie(domain: String, cookie: PersistentCookie) {
        if (getBoolean(REMEMBER) == true) {
            store.update { store ->
                val cookies = store?.cookies?.toMutableMap() ?: mutableMapOf()
                val domainCookies =
                    cookies[domain]?.filterNot { it.name == cookie.name }?.toMutableList() ?: mutableListOf()
                if (cookie.value.isNotEmpty()) {
                    //if cookie value is empty then it is a remove cookie call
                    domainCookies.add(cookie)
                }
                cookies[domain] = domainCookies
                store?.copy(cookies = cookies)
            }
        } else {
            val temp = tempCookies.value.toMutableMap()
            val domainCookies =
                temp[domain]?.filterNot { it.name == cookie.name }?.toMutableList() ?: mutableListOf()
            if (cookie.value.isNotEmpty()) {
                //if cookie value is empty then it is a remove cookie call
                domainCookies.add(cookie)
            }
            temp[domain] = domainCookies
            tempCookies.emit(temp)
        }
    }

    override suspend fun getAllCookies(domain: String): List<PersistentCookie> {
        val allCookies = mutableListOf<PersistentCookie>()
        val tempDomainCookies = tempCookies.value[domain] ?: listOf()
        val domainCookies = store.get()?.let { store ->
            store.cookies[domain]?.filter { it.value.isNotEmpty() }
        } ?: listOf()
        allCookies.addAll(tempDomainCookies)
        allCookies.addAll(domainCookies)
        return allCookies
    }

    override suspend fun removeAllCookies() {
        store.update { it?.copy(cookies = mapOf()) }
    }

    override suspend fun removeCookie(domain: String, cookieName: String) {
        val cookies = store.get()?.cookies?.toMutableMap() ?: mutableMapOf()
        val domainCookies = cookies[domain]?.toMutableList() ?: mutableListOf()
        if (domainCookies.isNotEmpty()) {
           val index = domainCookies.indexOfFirst { it.name == cookieName }
            if (index != -1) {
                domainCookies.removeAt(index)
                cookies[domain] = domainCookies
                store.update { store ->
                    store?.copy(cookies = cookies)
                }
            }
        }
    }

    private fun createStoreIfNotExist() = applicationIoScope.launch {
        val storePath = Path(platformConfiguration.storeDir)
        if (!storePath.exists()) {
            storePath.createParentDirectories()
            store.set(
                Store(
                    settings = mapOf(
                        REMEMBER to DEFAULT_REMEMBER_ME.toString(),
                        RAM to DEFAULT_RAM.toString()
                    )
                )
            )
        }
    }

    companion object {
        //default launcher settings
        private const val DEFAULT_REMEMBER_ME = true
        private const val DEFAULT_RAM: Double = 4.0
    }
}
