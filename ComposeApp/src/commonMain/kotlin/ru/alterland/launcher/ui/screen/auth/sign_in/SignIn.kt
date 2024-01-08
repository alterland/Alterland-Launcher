package ru.alterland.launcher.ui.screen.auth.sign_in

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.Res
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.*

@Composable
fun SignIn(
    state: SignInContract.State,
    setEvent: (e: SignInContract.Event) -> Unit,
    navigateToRecovery: () -> Unit,
    navigateToSignUp: () -> Unit
) {

    fun isQueryInProgress() = state.signInProgress || state.vkSignInProgress || state.googleSignInProgress

    val icLogin = painterResource(Res.image.ic_login)
    val icPassword = painterResource(Res.image.ic_password)
    val icVk = painterResource(Res.image.ic_vk)
    val icGoogle = painterResource(Res.image.ic_google)

    val elementsPadding = 20.dp

    Column {
        Input(
            hint = "Email",
            text = state.login,
            icon = icLogin,
            onInput = { setEvent(SignInContract.Event.OnLoginInput(it)) },
            enabled = true,
            singleLine = true
        )
        Input(
            hint = "Пароль",
            text = state.password,
            icon = icPassword,
            onInput = { setEvent(SignInContract.Event.OnPasswordInput(it)) },
            type = InputType.PASSWORD,
            singleLine = true,
            enabled = true,
            modifier = Modifier.padding(top = elementsPadding),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CheckBox(checked = state.remember, text = "Запомнить меня") {
                setEvent(SignInContract.Event.OnRememberMeChecked)
            }
            Text(
                text = "Забыли пароль?",
                //textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary,
                fontSize = 12.sp,
                modifier = Modifier.clickable(onClick = navigateToRecovery)
            )
        }
        Button(
            text = "Войти",
            isEnabled = !isQueryInProgress(),
            isLoading = state.signInProgress,
            onClick = { setEvent(SignInContract.Event.OnSignInClicked) },
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp)
        )
        SocialButton(
            "Войти через Вконтакте",
            icVk,
            isEnabled = !isQueryInProgress(),
            isLoading = state.vkSignInProgress,
            modifier = Modifier.padding(top = elementsPadding),
            onClick = { setEvent(SignInContract.Event.OnVkSignInClicked) },
        )
        SocialButton(
            "Войти через Google",
            icGoogle,
            isEnabled = !isQueryInProgress(),
            isLoading = state.googleSignInProgress,
            modifier = Modifier.padding(top = elementsPadding/2),
            onClick = { setEvent(SignInContract.Event.OnGoogleSignInClicked) },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = elementsPadding)
                .clickable(enabled = !isQueryInProgress(), onClick = navigateToSignUp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Нет аккаунта?",
                color = AppTheme.colors.labelSecondary,
                fontSize = 12.sp,
            )
            Text(
                text = "Регистрация",
                //textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

//@Preview
//@Composable
//fun SignInPreview() {
//    SignIn(SignInContract.State(), {}, {}, {})
//}
