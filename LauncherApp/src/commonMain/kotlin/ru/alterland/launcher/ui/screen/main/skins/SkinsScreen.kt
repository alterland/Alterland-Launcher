package ru.alterland.launcher.ui.screen.main.skins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SkinsScreen(
    payload: SkinsPayload,
    navigateBack: () -> Unit
) {
    val viewModel: SkinsViewModel = koinViewModel{ parametersOf(payload) }
    val state by viewModel.collectAsState()

    Skins(
        state = state,
        onAction = { viewModel.dispatch(it) }
    )
}
