package ru.alterland.launcher.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.alterland.launcher.domain.model.AppEvent
import ru.alterland.launcher.domain.repository.AppEventRepository

class AppEventRepositoryImpl(
    private val scope: CoroutineScope
): AppEventRepository {

    private val _events = MutableSharedFlow<AppEvent>()
    override val events = _events.asSharedFlow()

    override fun sendEvent(event: AppEvent) = scope.launch {
        _events.emit(event)
    }
}
