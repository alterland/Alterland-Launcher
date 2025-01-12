package ru.alterland.launcher.util.extentions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.handleErrors(
    onError: (Throwable) -> Unit,
): Flow<T> =
    catch {
            e -> onError(e)
    }
