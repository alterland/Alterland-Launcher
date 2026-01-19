package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.util.base.Resource

interface UserRepository {
    val user: StateFlow<Resource<User>>

    suspend fun signIn(login: String, password: String)
    suspend fun signUp(nickname: String, email: String, password: String)
    suspend fun resetPassword(email: String)
    suspend fun signOut()
    suspend fun checkNick(nickname: String): Boolean?
    suspend fun updateUser()
}
