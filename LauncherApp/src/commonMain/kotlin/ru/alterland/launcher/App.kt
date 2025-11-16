package ru.alterland.launcher

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.alterland.launcher.ui.screen.ContainerRoute
import ru.alterland.launcher.ui.screen.auth.container.AuthContainerScreen
import ru.alterland.launcher.ui.screen.containerRouteConfig
import ru.alterland.launcher.ui.screen.main.container.MainContainerScreen
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
@Preview
internal fun App(isDarkTheme: Boolean = true) = AppTheme(isDarkTheme) {

    val backStack = rememberNavBackStack(containerRouteConfig, ContainerRoute.Auth)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<ContainerRoute.Auth>(
                metadata = NavDisplay.transitionSpec {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(700)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(700)
                    )
                }
            ) {
                AuthContainerScreen(
                    navigateToMain = {
                        backStack.add(ContainerRoute.Main)
                        backStack.removeFirstOrNull()
                    }
                )
            }
            entry<ContainerRoute.Main>(
                metadata = NavDisplay.transitionSpec {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(700)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(700)
                    )
                }
            ) {
                MainContainerScreen(
                    navigateToAuth = {
                        backStack.add(ContainerRoute.Auth)
                        backStack.removeFirstOrNull()
                    }
                )
            }
        }
    )
}
