package ru.alterland.launcher.domain.repository

import ru.alterland.launcher.domain.model.clientprofile.LaunchParams

interface LaunchRepository {
    fun launch(options: LaunchParams)
}
