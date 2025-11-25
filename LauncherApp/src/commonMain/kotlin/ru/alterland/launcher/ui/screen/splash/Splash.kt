package ru.alterland.launcher.ui.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Logo

@Composable
fun Splash(
    state: SplashContract.State,
    onAction: (SplashContract.Action) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundSecondary),
    ) {
        Logo(modifier = Modifier.align(Alignment.Center))
    }
}
