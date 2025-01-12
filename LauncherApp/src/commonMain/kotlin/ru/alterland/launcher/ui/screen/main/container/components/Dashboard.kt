package ru.alterland.launcher.ui.screen.main.container.components

import androidx.compose.runtime.Composable
import ru.alterland.launcher.ui.screen.main.container.DashboardContract

@Composable
expect fun Dashboard(
    state: DashboardContract.State,
    childNavigation: @Composable () -> Unit,
    onEvent: (e: DashboardContract.Event) -> Unit,
    navigateToAddServer: () -> Unit
)
