package ru.alterland.launcher.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.alterland.launcher.domain.entity.AppError
import ru.alterland.launcher.domain.repository.ErrorRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ErrorRepositoryImpl: ErrorRepository {
    private val _errors: MutableStateFlow<List<AppError>> = MutableStateFlow(listOf())
    override val errors: StateFlow<List<AppError>> = _errors.asStateFlow()

    @ExperimentalUuidApi
    override suspend fun addError(throwable: Throwable) {
        val appError = AppError(
            id = Uuid.random().toString(),
            error = throwable
        )
        errors.value.toMutableList().apply {
            add(appError)
            _errors.emit(this)
        }
    }

    override suspend fun removeError(id: String) {
        errors.value.toMutableList().apply {
            find { it.id == id }?.let {
                remove(it)
            }
            _errors.emit(this)
        }
    }

    override suspend fun clearErrors() {
        _errors.emit(listOf())
    }
}
