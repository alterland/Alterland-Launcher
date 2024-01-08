package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.entity.User

interface UserRepository {
    suspend fun signIn(login: String, password: String)
    suspend fun signUp(nickname: String, email: String, password: String)
    suspend fun resetPassword(email: String)
    suspend fun signOut()
    suspend fun checkNick(nickname: String): Boolean?
    suspend fun getUser(force: Boolean = false): User
}
