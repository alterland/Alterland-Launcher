package ru.alterland.launchercore.domain.repository

import ru.alterland.launchercore.data.source.network.model.response.ClientProfileResponse

interface MojangRepository {

    suspend fun getClientProfile(id: String? = null, snapshot: Boolean = false): ClientProfileResponse
}
