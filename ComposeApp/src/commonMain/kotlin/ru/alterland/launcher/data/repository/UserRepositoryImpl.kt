package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.local.LocalStoreFields.ACCESS_TOKEN
import ru.alterland.launcher.data.source.network.UserApi
import ru.alterland.launcher.data.source.network.model.request.ResetPasswordRequest
import ru.alterland.launcher.data.source.network.model.request.SignInRequest
import ru.alterland.launcher.data.source.network.model.request.SignUpRequest
import ru.alterland.launcher.domain.entity.User
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.util.base.AppException

class UserRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher,
    private val userApi: UserApi,
    private val localStorage: LocalStorage
): UserRepository {

    private var cachedUser: User? = null

    override suspend fun signIn(login: String, password: String) {
        withContext(dispatcherIo) {
            val result = userApi.signIn(
                SignInRequest(
                    login = login,
                    password = password
                )
            )
            result.accessToken?.let {
                localStorage.store(ACCESS_TOKEN, it)
            }
            result.toDomain().also {
                cachedUser = it
            }
        }
    }

    override suspend fun signUp(nickname: String, email: String, password: String) {
        withContext(dispatcherIo) {
            val result = userApi.signUp(
                SignUpRequest(
                    nickname = nickname,
                    email = email,
                    password = password
                )
            )
            result.accessToken?.let {
                localStorage.store(ACCESS_TOKEN, it)
            }
            result.toDomain().also {
                cachedUser = it
            }
        }
    }

    override suspend fun resetPassword(email: String) {
        withContext(dispatcherIo) {
            val result = userApi.resetPassword(ResetPasswordRequest(email = email))
            result.status.value in 200..299
        }
    }

    override suspend fun signOut() {
        withContext(dispatcherIo) {
            runCatching {
                userApi.signOut()
            }.onFailure {
                //do nothing
            }
            localStorage.remove(ACCESS_TOKEN)
            cachedUser = null
        }
    }

    override suspend fun checkNick(nickname: String): Boolean? = withContext(dispatcherIo) {
        try {
            userApi.checkNick(nickname)
            true
        } catch (e: Exception) {
            when(e) {
                is AppException.UserNotFoundException -> false
                is AppException.ValidationException -> true
                else -> null
            }
        }
    }

    override suspend fun getUser(force: Boolean) = withContext(dispatcherIo) {
        if (force) {
            fetchUser()
        } else {
            cachedUser ?: fetchUser()
        }
    }

    private suspend fun fetchUser() = userApi.getUser().toDomain().also { cachedUser = it }
}
