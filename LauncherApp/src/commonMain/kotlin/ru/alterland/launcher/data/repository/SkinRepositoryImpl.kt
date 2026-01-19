package ru.alterland.launcher.data.repository

import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.readByteArray
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.mapper.toDto
import ru.alterland.launcher.data.source.network.SkinsApi
import ru.alterland.launcher.data.source.network.model.request.UploadCustomSkinRequest
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.repository.SkinRepository

class SkinRepositoryImpl(
    private val skinsApi: SkinsApi,
    private val fileSystem: FileSystem
) : SkinRepository {

    override suspend fun getLibrarySkins(): List<Skin> {
        return skinsApi.getLibrarySkins().map { it.toDomain() }
    }

    override suspend fun setUserSkin(skinId: String): Skin {
        return skinsApi.setUserSkin(skinId).toDomain()
    }

    override suspend fun uploadCustomSkin(filePath: String, modelType: Skin.ModelType) {
        val uploadResponse = skinsApi.getUploadUrl(UploadCustomSkinRequest(modelType = modelType.toDto()))
        val fileBytes = fileSystem.source(Path(filePath)).buffered().readByteArray()
        skinsApi.uploadToPresignedUrl(uploadResponse.uploadUrl, fileBytes)
    }
}
