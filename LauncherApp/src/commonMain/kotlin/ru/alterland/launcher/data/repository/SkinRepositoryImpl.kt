package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.repository.SkinRepository

class SkinRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher
): SkinRepository {

    override suspend fun loadSkin(serverId: String): Skin = withContext(dispatcherIo) {
        Skin(
            name = "Test",
            url = "https://www.minecraftskins.com/uploads/skins/2025/12/19/url-23728982.png?v938",
            modelType = Skin.ModelType.WIDE
        )
    }

    override suspend fun loadSkins(serverId: String): List<Skin> = withContext(dispatcherIo) {
        listOf(
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/12/19/url-23728982.png?v938",
                modelType = Skin.ModelType.WIDE
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/11/03/blue-shirt-boy-23621399.png?v938",
                modelType = Skin.ModelType.WIDE
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/10/12/bank-manager-23578245.png?v938",
                modelType = Skin.ModelType.WIDE
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/10/10/url-23572681.png?v938",
                modelType = Skin.ModelType.WIDE
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/12/12/donald-trumpesito-23712718.png?v938",
                modelType = Skin.ModelType.WIDE
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/12/20/childish-young-girl-23730831.png?v938",
                modelType = Skin.ModelType.SLIM
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/12/20/---rosy---rce-23730384.png?v938",
                modelType = Skin.ModelType.SLIM
            ),
            Skin(
                name = "Test",
                url = "https://www.minecraftskins.com/uploads/skins/2025/12/20/bea-23730387.png?v938",
                modelType = Skin.ModelType.SLIM
            )
        )
    }

    override suspend fun saveSkin(serverId: String, skin: Skin) {
        TODO("Not yet implemented")
    }
}
