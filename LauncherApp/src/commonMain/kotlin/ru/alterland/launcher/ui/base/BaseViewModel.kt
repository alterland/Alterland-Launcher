package ru.alterland.launcher.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.ContainerHost
import ru.alterland.launcher.domain.repository.ErrorRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlin.getValue

abstract class BaseViewModel<
    STATE : UiState,
    SIDE_EFFECT : UiEffect,
    ACTION : UiAction
>: ContainerHost<STATE, SIDE_EFFECT>, ViewModel(), KoinComponent {

    protected val errorRepository: ErrorRepository by inject()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        if (exception !is CancellationException) {
            viewModelScope.launch {
                errorRepository.addError(exception)
            }
        }
    }

    protected val viewModelScopeErrorHandled = viewModelScope + exceptionHandler

    abstract fun dispatch(action: ACTION)
}
