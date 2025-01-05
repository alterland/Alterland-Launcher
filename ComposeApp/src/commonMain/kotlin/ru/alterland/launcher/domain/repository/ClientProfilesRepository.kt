package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.ClientProfileObject

interface ClientProfilesRepository {
    suspend fun getClientProfileObjects(): List<ClientProfileObject>
}
