package ru.alterland.launcher.ui.screen.main.servers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ServersScreen(
    viewModel: ServersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    Servers(state = state)
}
