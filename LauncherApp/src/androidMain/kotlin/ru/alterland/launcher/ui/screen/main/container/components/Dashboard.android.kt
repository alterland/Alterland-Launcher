package ru.alterland.launcher.ui.screen.main.container.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import ru.alterland.launcher.ui.screen.main.container.DashboardContract

@Composable
actual fun Dashboard(
    state: DashboardContract.State,
    childNavigation: @Composable (() -> Unit),
    onEvent: (DashboardContract.Event) -> Unit,
    navigateToAddServer: () -> Unit
) {
    Text(text = "dashboard")
}
