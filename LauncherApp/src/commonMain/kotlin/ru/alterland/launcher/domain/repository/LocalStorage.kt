package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launcher.domain.model.Store

interface LocalStorage {
    val accessToken: StateFlow<String?>
    suspend fun setAccessToken(accessToken: String?)

    val storeFlow: Flow<Store?>
    suspend fun get(): Store?
    suspend fun update(operation: (Store?) -> Store?)
}
