package ru.alterland.launcher.data.source.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import ru.alterland.launcher.data.source.network.model.request.ResetPasswordRequest
import ru.alterland.launcher.data.source.network.model.request.SignInRequest
import ru.alterland.launcher.data.source.network.model.request.SignUpRequest
import ru.alterland.launcher.data.source.network.model.response.CheckNicknameResponse
import ru.alterland.launcher.data.source.network.model.response.GetUserResponse

class UserApi(private val httpClient: HttpClient) {
    companion object {
        private const val PATH: String = "/user"
    }

    suspend fun signIn(req: SignInRequest): GetUserResponse = httpClient.post("$PATH/signin") {
        setBody(req)
    }.body()

    suspend fun signUp(req: SignUpRequest): GetUserResponse = httpClient.post("$PATH/signup") {
        setBody(req)
    }.body()

    suspend fun resetPassword(req: ResetPasswordRequest): HttpResponse = httpClient.post("$PATH/reset-password") {
        setBody(req)
    }

    suspend fun checkNick(nickname: String): CheckNicknameResponse = httpClient.get("$PATH/check/$nickname").body()

    suspend fun getUser(): GetUserResponse = httpClient.get(PATH).body()

    suspend fun signOut() = httpClient.get("$PATH/signout")
}
