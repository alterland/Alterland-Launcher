package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.clientprofile.ClientProfile

interface DownloadRepository {
    suspend fun update(clientProfile: ClientProfile)
    suspend fun toggleUpdate()
}
