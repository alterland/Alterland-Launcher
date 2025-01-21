package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.Feature
import ru.alterland.launcher.domain.model.clientprofile.Player

interface ClientFilesRepository {
    fun updateAndLaunch(
        clientProfile: ClientProfile,
        player: Player,
        features: Map<Feature, Boolean>
    )
}
