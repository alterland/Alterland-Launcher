package ru.alterland.launcher.data.source.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.source.network.model.request.SetUserSkinRequest
import ru.alterland.launcher.data.source.network.model.request.UploadCustomSkinRequest
import ru.alterland.launcher.data.source.network.model.response.SkinResponse
import ru.alterland.launcher.data.source.network.model.response.UploadUrlResponse

class SkinsApi(
    private val httpClient: HttpClient,
    private val uploadHttpClient: HttpClient,
    private val dispatcherIo: CoroutineDispatcher
) {
    companion object {
        private const val PATH = "game/skins"
    }

    suspend fun getLibrarySkins(): List<SkinResponse> = withContext(dispatcherIo) {
        httpClient.get("$PATH/library").body()
    }

    suspend fun getUserSkin(): SkinResponse = withContext(dispatcherIo) {
        httpClient.get("$PATH/my").body()
    }

    suspend fun setUserSkin(skinId: String): SkinResponse = withContext(dispatcherIo) {
        httpClient.put("$PATH/my") {
            setBody(SetUserSkinRequest(skinId))
        }.body()
    }

    suspend fun getUploadUrl(request: UploadCustomSkinRequest): UploadUrlResponse = withContext(dispatcherIo) {
        httpClient.post("$PATH/upload") {
            setBody(request)
        }.body()
    }

    suspend fun uploadToPresignedUrl(url: String, fileBytes: ByteArray): Unit = withContext(dispatcherIo) {
        uploadHttpClient.put(url) {
            contentType(ContentType.Image.PNG)
            header("x-amz-acl", "public-read")
            setBody(fileBytes)
        }
    }
}
