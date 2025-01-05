package ru.alterland.launchercore.domain.repository

import ru.alterland.launchercore.domain.model.LaunchOptions

interface LaunchRepository {

    fun launch(options: LaunchOptions)
}
