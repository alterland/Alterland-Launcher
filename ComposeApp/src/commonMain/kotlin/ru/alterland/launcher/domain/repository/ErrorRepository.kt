package ru.alterland.launcher.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.alterland.launcher.domain.entity.AppError

interface ErrorRepository {
    suspend fun addError(throwable: Throwable)
    suspend fun removeError(id: String)
    suspend fun clearErrors()
    val errors: StateFlow<List<AppError>>
}
