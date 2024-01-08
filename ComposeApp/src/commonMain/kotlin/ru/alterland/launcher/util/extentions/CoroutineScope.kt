package ru.alterland.launcher.util.extentions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.launchSafe(
    onError: (Throwable) -> Unit,
    onSuccess: () -> Unit = {},
    onComplete: () -> Unit = {},
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    action: suspend CoroutineScope.() -> Unit) = launch(dispatcher) {
        try {
            action(this)
            onSuccess()
        } catch (e: Throwable) {
            if (e !is CancellationException) onError(e)
        } finally {
            onComplete()
        }
    }
