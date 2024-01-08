package ru.alterland.launcher.ui.base

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alterland.launcher.domain.repository.ErrorRepository

abstract class BaseScreenModel<Event : UiEvent, State : UiState, Effect : UiEffect>(
    initialState: State
): StateScreenModel<State>(initialState), KoinComponent {

    val errorRepository: ErrorRepository by inject()

    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect : Channel<Effect> = Channel() //for one-time actions like toasts
    val effect = _effect.receiveAsFlow()

    init {
        event.onEach { e ->
            handleEvent(e)
        }.launchIn(screenModelScope)
    }

    abstract fun handleEvent(event: Event)

    fun setEvent(event : Event) =
        screenModelScope.launch { _event.emit(event) }

    protected inline fun setState(reduce: State.() -> State) {
        mutableState.value = state.value.reduce()
    }

    protected fun setEffect(builder: () -> Effect) =
        screenModelScope.launch { _effect.send(builder()) }

    open fun onError(throwable: Throwable) {
        print("Error: $throwable")
        screenModelScope.launch {
            errorRepository.addError(throwable)
        }
    }
}
