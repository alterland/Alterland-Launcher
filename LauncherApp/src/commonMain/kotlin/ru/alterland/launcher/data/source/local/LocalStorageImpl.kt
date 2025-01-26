package ru.alterland.launcher.data.source.local

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.extensions.storeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.mapper.toVersion
import ru.alterland.launcher.data.source.local.model.StoreV1
import ru.alterland.launcher.domain.model.Store
import ru.alterland.launcher.domain.repository.LocalStorage

class LocalStorageImpl(
    private val fileSystem: FileSystem,
    private val applicationIoScope: CoroutineScope,
    private val platformConfiguration: PlatformConfiguration
) : LocalStorage {

    private val path = Path(platformConfiguration.storeDir)

    private val store: KStore<StoreV1> = storeOf(
        file = path,
        version = 1,
        default = StoreV1(),
        enableCache = true
    )

    private val _accessToken: MutableStateFlow<String> = MutableStateFlow("")
    override val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    override val storeFlow: Flow<Store?> = store.updates.map { it?.toDomain() }

    init {
        applicationIoScope.launch {
            createStoreDirIfNotExist()
            setAccessToken(store.get()?.accessToken.orEmpty())
        }
    }

    override suspend fun setAccessToken(accessToken: String) {
        val currentStore = get()
        if (currentStore?.rememberMe == true) {
            update { it?.copy(accessToken = accessToken) }
        }
        _accessToken.emit(accessToken)
    }

    override suspend fun get(): Store? = store.get()?.toDomain()

    override suspend fun update(operation: (Store?) -> Store?) {
        update(true, operation)
    }

    private suspend fun update(retryOnFail: Boolean, operation: (Store?) -> Store?) {
        try {
            store.update { operation.invoke(it?.toDomain())?.toVersion() }
        } catch (_: FileNotFoundException) {
            if (retryOnFail) {
                createStoreDirIfNotExist()
                update(false, operation)
            }
        }
    }

    private fun createStoreDirIfNotExist() {
        val storePath = Path(platformConfiguration.storeDir)
        if (!fileSystem.exists(storePath)) {
            storePath.parent?.let { fileSystem.createDirectories(it) }
        }
    }
}
