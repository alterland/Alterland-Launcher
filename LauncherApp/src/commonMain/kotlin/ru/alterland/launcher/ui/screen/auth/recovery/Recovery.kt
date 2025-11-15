package ru.alterland.launcher.ui.screen.auth.recovery

import alterlandlauncher.launcherapp.generated.resources.*
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
    onAction: (RecoveryContract.Action) -> Unit
) {

    Column(
        modifier = Modifier.padding(top = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Input(
            text = state.email,
            hint = stringResource(Res.string.email),
            icon = painterResource(Res.drawable.ic_login),
            onInput = { onAction(RecoveryContract.Action.OnEmailInput(it)) }
        )
        Button(
            text = stringResource(Res.string.string_continue),
            isEnabled = !state.sendCodeProgress,
            isLoading = state.sendCodeProgress,
            modifier = Modifier.fillMaxWidth()
        ) {  onAction(RecoveryContract.Action.OnResetPasswordClick) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .clickable(onClick = { onAction(RecoveryContract.Action.OnBackClick) }),
                text = stringResource(Res.string.back),
                color = AppTheme.colors.labelSecondary,
                fontSize = 12.sp,
            )
        }
    }
}
