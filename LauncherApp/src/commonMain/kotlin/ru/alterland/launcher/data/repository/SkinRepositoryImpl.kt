package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.alterland.launcher.data.source.network.SkinsApi
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.repository.SkinRepository


class SkinRepositoryImpl(
    private val skinsApi: SkinsApi,
    private val dispatcherIo: CoroutineDispatcher
): SkinRepository {

    override suspend fun loadSkin(): Skin {
        TODO("Not yet implemented")
    }

    override suspend fun saveSkin(skin: Skin) {
        TODO("Not yet implemented")
    }
}
