package ru.alterland.launcher.ui.screen.main.clientsettings

import androidx.compose.runtime.Composable

@Composable
expect fun ClientSettingsScreen(
    payload: ClientSettingsPayload,
    navigateBack: () -> Unit
)
