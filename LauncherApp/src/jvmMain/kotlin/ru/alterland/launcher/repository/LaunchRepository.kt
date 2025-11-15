package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.clientprofile.ClientProfile
import ru.alterland.launcher.domain.model.clientprofile.Feature
import ru.alterland.launcher.domain.model.clientprofile.Player

interface LaunchRepository {
    suspend fun launch(
        clientProfile: ClientProfile,
        player: Player,
        features: Map<Feature, Boolean>
    )
}
