package ru.alterland.launcher.ui.screen.auth.container

import alterlandlauncher.composeapp.generated.resources.Res
import alterlandlauncher.composeapp.generated.resources.background_auth
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.ui.screen.auth.sign_in.SignInScreen
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Logo
import ru.alterland.launcher.ui.widgets.errors.BaseErrorHandler

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun AuthContainer(
    state: AuthContainerContract.State,
    onEvent: (e: AuthContainerContract.Event) -> Unit,
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 540.dp
        val authWidth = if (isMobile) 1f else 0.54f
        if (!isMobile) {
            Image(
                painter = painterResource(Res.drawable.background_auth),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight()
            )
        }
        Row(Modifier.fillMaxWidth()) {
            if (!isMobile) {
                Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
                    BaseErrorHandler(
                        modifier = Modifier.fillMaxHeight().padding(bottom = 10.dp, top = 50.dp),
                        itemsModifier = Modifier.padding(vertical = 3.dp, horizontal = 15.dp),
                        errors = state.errors,
                        onMessageClose = { onEvent(AuthContainerContract.Event.OnMessageClose(it)) }
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(authWidth)
                    .background(AppTheme.colors.backgroundSecondary)
                    .padding(
                        start = 70.dp,
                        end = 70.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo(modifier = Modifier.padding(top = 50.dp, bottom = 26.dp))

                Navigator(SignInScreen()) { navigator ->
                    FadeTransition(
                        navigator = navigator,
                        disposeScreenAfterTransitionEnd = true
                    )
                }
            }
        }
    }
}
