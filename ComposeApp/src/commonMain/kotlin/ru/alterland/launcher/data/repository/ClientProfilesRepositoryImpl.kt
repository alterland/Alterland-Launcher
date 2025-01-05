package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.alterland.launcher.data.mapper.toDomain
import ru.alterland.launcher.data.source.ClientProfilesApi
import ru.alterland.launcher.domain.model.ClientProfileObject
import ru.alterland.launcher.domain.repository.ClientProfilesRepository

class ClientProfilesRepositoryImpl(
    private val clientProfilesApi: ClientProfilesApi,
    private val dispatcherDefault: CoroutineDispatcher
): ClientProfilesRepository {

    override suspend fun getClientProfileObjects(): List<ClientProfileObject> = withContext(dispatcherDefault) {
        clientProfilesApi.getClientProfileObjects().map { it.toDomain() }
    }
}
