package ru.alterland.launcher.ui.screen.auth.recovery

import alterlandlauncher.composeapp.generated.resources.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.Input

@Composable
fun Recovery(
    state: RecoveryContract.State,
    onEvent: (e: RecoveryContract.Event) -> Unit,
    navigateBack: () -> Unit
) {

    val top = 18.dp

    Column {
        Input(
            text = state.email,
            hint = stringResource(Res.string.email),
            icon = painterResource(Res.drawable.ic_login),
            modifier = Modifier.padding(top = top),
            onInput = { onEvent(RecoveryContract.Event.OnEmailInput(it)) }
        )
        Button(
            text = stringResource(Res.string.string_continue),
            isEnabled = !state.sendCodeProgress,
            isLoading = state.sendCodeProgress,
            modifier = Modifier.fillMaxWidth().padding(top = top)
        ) {  onEvent(RecoveryContract.Event.OnResetPasswordClicked) }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = top),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.back),
                color = AppTheme.colors.labelSecondary,
                fontSize = 12.sp,
                modifier = Modifier.clickable(onClick = navigateBack)
            )
        }
    }
}