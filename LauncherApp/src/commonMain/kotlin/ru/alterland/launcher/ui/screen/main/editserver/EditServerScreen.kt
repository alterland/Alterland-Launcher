package ru.alterland.launcher.ui.screen.main.editserver

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.parameter.parameterSetOf

data class EditServerScreen(private val payload: EditServerPayload): Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<EditServerScreenModel>(parameters = { parameterSetOf(payload) })
        val state by screenModel.state.collectAsState()
        val effects by screenModel.effects.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        effects.firstOrNull()?.let { effect ->
            LaunchedEffect(effect) {
                when (effect) {
                    EditServerContract.Effect.OnNavigateBack -> navigator.pop()
                }
            }
            screenModel.onEffectHandled(effect)
        }

        EditServer(
            state = state,
            onEvent = { e -> screenModel.onEvent(e) }
        )
    }
}
