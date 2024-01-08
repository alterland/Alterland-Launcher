package ru.alterland.launcher

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import ru.alterland.launcher.ui.screen.ContainerScreenProvider
import ru.alterland.launcher.ui.screen.auth.authScreenModule
import ru.alterland.launcher.ui.screen.containerScreenModule
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
internal fun App(
    isDarkTheme: Boolean = true
) = AppTheme(isDarkTheme) {

    ScreenRegistry {
        containerScreenModule()
        authScreenModule()
    }

    val authContainer = rememberScreen(ContainerScreenProvider.Auth)

    Navigator(authContainer) { navigator ->
        SlideTransition(navigator)
    }
}

internal expect fun openUrl(url: String?)
