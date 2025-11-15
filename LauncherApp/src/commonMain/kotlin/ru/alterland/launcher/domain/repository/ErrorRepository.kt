package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launcher.domain.model.AppError

interface ErrorRepository {
    val errors: StateFlow<List<AppError>>
    suspend fun addError(throwable: Throwable)
    suspend fun removeError(id: String)
    suspend fun clearErrors()
}
