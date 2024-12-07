package ru.alterland.launcher.data.source.local

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.SerializationException
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.source.local.LocalStoreFields.RAM
import ru.alterland.launcher.data.source.local.LocalStoreFields.REMEMBER
import ru.alterland.launcher.domain.repository.LocalStorage

class LocalStorageImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val applicationIoScope: CoroutineScope,
    private val platformConfiguration: PlatformConfiguration
) : LocalStorage {

    private val path = Path(platformConfiguration.storeDir)

    private val store: KStore<Map<String, String>> = storeOf(
        file = path,
        enableCache = true,
        default = mapOf(
            REMEMBER to DEFAULT_REMEMBER_ME.toString(),
            RAM to DEFAULT_RAM.toString()
        )
    )

    override val storeFlow: Flow<Map<String, String>> = store.updates.map { it ?: mapOf() }

    init {
        applicationIoScope.launch {
            createStoreDirIfNotExist()
            runMigrations()
        }
    }

    override suspend fun getAll(): Map<String, String> = store.get() ?: mapOf()

    override suspend fun store(key: String, value: Any) {
        store(key, value, true)
    }

    override suspend fun remove(key: String) {
        try {
            store.update { store ->
                store?.toMutableMap()?.also {
                    it.remove(key)
                }
            }
        } catch (e: FileNotFoundException) {
            createStoreDirIfNotExist()
        }
    }

    private suspend fun store(key: String, value: Any, retryOnFail: Boolean): Unit = withContext(dispatcherIo) {
        try {
            store.update { store ->
                store?.toMutableMap()?.also {
                    it[key] = value.toString()
                }
            }
        } catch (e: FileNotFoundException) {
            if (retryOnFail) {
                createStoreDirIfNotExist()
                store(key, value, false)
            }
        }
    }

    override suspend fun getString(key: String) = store.get()?.let { store ->
        store[key]
    }

    override suspend fun getBoolean(key: String) = store.get()?.let { store ->
        store[key]?.toBooleanStrictOrNull()
    }

    override suspend fun getDouble(key: String) = store.get()?.let { store ->
        store[key]?.toDoubleOrNull()
    }

    private fun createStoreDirIfNotExist() {
        val storePath = Path(platformConfiguration.storeDir)
        with(SystemFileSystem) {
            if(!exists(storePath)) {
                storePath.parent?.let { createDirectories(it) }
            }
        }
    }

    private suspend fun runMigrations() {
        try {
            store.get()
        } catch (e: SerializationException) {
            with(SystemFileSystem) {
                delete(path)
            }
        }
    }

    companion object {
        //default launcher settings
        private const val DEFAULT_REMEMBER_ME = true
        private const val DEFAULT_RAM = 4.0
    }
}
