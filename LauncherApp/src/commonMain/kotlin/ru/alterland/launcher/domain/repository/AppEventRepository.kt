package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import ru.alterland.launcher.domain.model.AppEvent

interface AppEventRepository {

    val events: SharedFlow<AppEvent>

    fun sendEvent(event: AppEvent): Job
}
