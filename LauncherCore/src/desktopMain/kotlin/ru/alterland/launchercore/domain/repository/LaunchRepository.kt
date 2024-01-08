package ru.alterland.launchercore.domain.repository

import ru.alterland.launchercore.dto.LaunchOptions

interface LaunchRepository {

    fun launch(options: LaunchOptions)
}
