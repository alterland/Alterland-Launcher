package ru.alterland.launcher.ui.screen.auth.sign_in

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.CheckBox
import ru.alterland.launcher.ui.widgets.Input
import ru.alterland.launcher.ui.widgets.InputType

@Composable
fun SignIn(
    state: SignInContract.State,
    onAction: (SignInContract.Action) -> Unit
) {

    fun isQueryInProgress() = state.signInProgress || state.vkSignInProgress || state.googleSignInProgress

    val icLogin = painterResource(Res.drawable.ic_login)
    val icPassword = painterResource(Res.drawable.ic_password)
    painterResource(Res.drawable.ic_vk)
    painterResource(Res.drawable.ic_google)

    val elementsPadding = 20.dp

    Column {
        Input(
            hint = stringResource(Res.string.email),
            text = state.login,
            icon = icLogin,
            onInput = { onAction(SignInContract.Action.OnLoginInput(it)) },
            enabled = true,
            singleLine = true
        )
        Input(
            hint = stringResource(Res.string.password),
            text = state.password,
            icon = icPassword,
            onInput = { onAction(SignInContract.Action.OnPasswordInput(it)) },
            type = InputType.PASSWORD,
            singleLine = true,
            enabled = true,
            modifier = Modifier.padding(top = elementsPadding),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CheckBox(checked = state.remember, text = stringResource(Res.string.remember_me)) {
                onAction(SignInContract.Action.OnRememberMeChecked)
            }
            Text(
                modifier = Modifier
                    .clickable(onClick = { onAction(SignInContract.Action.OnNavigateToRecovery) }),
                text = stringResource(Res.string.forgot_password),
                //textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary,
                fontSize = 12.sp
            )
        }
        Button(
            text = stringResource(Res.string.sign_in),
            isEnabled = !isQueryInProgress(),
            isLoading = state.signInProgress,
            onClick = { onAction(SignInContract.Action.OnSignInClicked) },
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp)
        )
//        SocialButton(
//            "Войти через Вконтакте",
//            icVk,
//            isEnabled = !isQueryInProgress(),
//            isLoading = state.vkSignInProgress,
//            modifier = Modifier.padding(top = elementsPadding),
//            onClick = { onEvent(SignInContract.Event.OnVkSignInClicked) },
//        )
//        SocialButton(
//            "Войти через Google",
//            icGoogle,
//            isEnabled = !isQueryInProgress(),
//            isLoading = state.googleSignInProgress,
//            modifier = Modifier.padding(top = elementsPadding/2),
//            onClick = { onEvent(SignInContract.Event.OnGoogleSignInClicked) },
//        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = elementsPadding)
                .clickable(
                    enabled = !isQueryInProgress(),
                    onClick = { onAction(SignInContract.Action.OnNavigateToSignUp) }
                ),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.no_account),
                color = AppTheme.colors.labelSecondary,
                fontSize = 12.sp,
            )
            Text(
                text = stringResource(Res.string.sign_up),
                //textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun SignInPreview() {
    SignIn(SignInContract.State()) {}
}
