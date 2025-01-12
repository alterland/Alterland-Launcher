package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    val storeFlow: Flow<Map<String, String>>
    suspend fun getAll(): Map<String, String>
    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?
    suspend fun getDouble(key: String): Double?
    suspend fun store(key: String, value: Any)
    suspend fun remove(key: String)
}
