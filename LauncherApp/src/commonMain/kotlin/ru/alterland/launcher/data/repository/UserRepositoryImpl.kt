package ru.alterland.launcher.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.network.UserApi
import ru.alterland.launcher.data.source.network.model.request.ResetPasswordRequest
import ru.alterland.launcher.data.source.network.model.request.SignInRequest
import ru.alterland.launcher.data.source.network.model.request.SignUpRequest
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.util.base.Resource

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val localStorage: LocalStorage
): UserRepository {

    private val _user: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Idle())
    override val user: StateFlow<Resource<User>> = _user.asStateFlow()

    override suspend fun signIn(login: String, password: String) {
        runCatching {
            _user.emit(Resource.Loading())
            val result = userApi.signIn(
                SignInRequest(
                    login = login,
                    password = password
                )
            )
            localStorage.setAccessToken(result.accessToken.orEmpty())
            val user = result.toDomain()
            _user.emit(Resource.Content(user))
        }.onFailure {
            _user.emit(Resource.Error(data = user.value.getOrNull(), throwable = it))
        }
    }

    override suspend fun signUp(nickname: String, email: String, password: String) {
        runCatching {
            val result = userApi.signUp(
                SignUpRequest(
                    nickname = nickname,
                    email = email,
                    password = password
                )
            )
            localStorage.setAccessToken(result.accessToken.orEmpty())
            val user = result.toDomain()
            _user.emit(Resource.Content(user))
        }.onFailure {
            _user.emit(Resource.Error(data = user.value.getOrNull(), throwable = it))
        }
    }

    override suspend fun resetPassword(email: String) {
        val result = userApi.resetPassword(ResetPasswordRequest(email = email))
        result.status.value in 200..299
    }

    override suspend fun signOut() {
        runCatching {
            userApi.signOut()
        }
        localStorage.setAccessToken("")
        userApi.clearToken()
        _user.emit(Resource.Idle())
    }

    override suspend fun checkNick(nickname: String): Boolean? = try {
        userApi.checkNick(nickname)
        true
    } catch (e: Exception) {
        null
//        when(e) {
//            is AppException.UserNotFoundException -> false
//            is AppException.ValidationException -> true
//            else -> null
//        }
    }

    override suspend fun updateUser() {
        runCatching {
            val user = userApi.getUser().toDomain()
            _user.emit(Resource.Content(user))
        }.onFailure {
            _user.emit(Resource.Error(data = user.value.getOrNull(), throwable = it))
        }
    }

}
