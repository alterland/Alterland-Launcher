package ru.alterland.launcher.ui.base

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alterland.launcher.domain.repository.ErrorRepository

abstract class BaseScreenModel<Event : UiEvent, State : UiState, Effect : UiEffect>(
    initialState: State
): StateScreenModel<State>(initialState), KoinComponent {

    val errorRepository: ErrorRepository by inject()

    private val _effects: MutableStateFlow<List<Effect>> = MutableStateFlow(emptyList())
    val effects: StateFlow<List<Effect>> = _effects.asStateFlow() //for one-time actions like navigation

    abstract fun onEvent(event: Event)

    protected inline fun setState(reduce: State.() -> State) {
        mutableState.value = state.value.reduce()
    }

    protected fun setEffect(builder: () -> Effect) = _effects.update { it.plus(builder()) }

    fun onEffectHandled(handledEffect: Effect) = _effects.update {
        it.filterNot { effect ->
            effect.uniqueId == handledEffect.uniqueId
        }
    }

    open fun onError(throwable: Throwable) {
        print("Error: $throwable")
        screenModelScope.launch {
            errorRepository.addError(throwable)
        }
    }
}
