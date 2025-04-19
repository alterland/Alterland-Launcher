package ru.alterland.launcher.data.source.local

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.extensions.storeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json
import ru.alterland.launcher.PlatformConfiguration
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.mapper.toVersion
import ru.alterland.launcher.data.source.local.model.StoreV1
import ru.alterland.launcher.domain.model.Store
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.util.extentions.v

class LocalStorageImpl(
    private val applicationIoScope: CoroutineScope,
    private val platformConfiguration: PlatformConfiguration,
    private val json: Json,
    private val fileSystem: FileSystem,
) : LocalStorage {

    private val storePath = Path(platformConfiguration.defaultDir v "store.json")

    private val store: KStore<StoreV1> = storeOf(
        file = storePath,
        version = 1,
        default = StoreV1(),
        enableCache = true,
        json = json
    )

    private val _accessToken: MutableStateFlow<String?> = MutableStateFlow(null)
    override val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    override val storeFlow: Flow<Store?> = store.updates.map { it?.toDomain() }

    init {
        applicationIoScope.launch {
            createStoreDirIfNotExist()
            setAccessToken(store.get()?.accessToken)
        }
    }

    override suspend fun setAccessToken(accessToken: String?) {
        if (store.get()?.rememberMe == true) {
            update { it?.copy(accessToken = accessToken) }
        }
        _accessToken.emit(accessToken)
    }

    override suspend fun get(): Store? = store.get()?.toDomain()

    override suspend fun update(operation: (Store?) -> Store?) {
        store.update { operation.invoke(it?.toDomain())?.toVersion() }
    }

    private fun createStoreDirIfNotExist() {
        if (!fileSystem.exists(storePath)) {
            storePath.parent?.let { fileSystem.createDirectories(it) }
        }
    }
}
