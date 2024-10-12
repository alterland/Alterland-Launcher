package ru.alterland.launcher.ui.screen.auth.sign_up

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.util.base.Resource
import ru.alterland.launcher.Res
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.Input
import ru.alterland.launcher.ui.widgets.InputType
import ru.alterland.launcher.ui.widgets.SocialButton

@Composable
fun SignUp(
    state: SignUpContract.State,
    setEvent: (e: SignUpContract.Event) -> Unit,
    navigateBack: () -> Unit
) {

    fun isIOInProgress() = state.signUpProgress || state.vkSignUpProgress || state.googleSignUpProgress

    val elementsPadding = 22.dp

    val icNickOk = painterResource(Res.image.ic_check)
    val icNickBusy = painterResource(Res.image.ic_dont)
    val icEmail = painterResource(Res.image.ic_email)
    val icPassword = painterResource(Res.image.ic_password)
    val icVk = painterResource(Res.image.ic_vk)
    val icGoogle = painterResource(Res.image.ic_google)

    Column {
        Input(
            hint = "Ник",
            text = state.nickName,
            icon = painterResource(Res.image.ic_login),
            enabled = !isIOInProgress(),
            isLoading = state.checkNickQuery is Resource.Loading,
            endIcon = when(state.checkNickQuery) {
                is Resource.Content -> when(state.checkNickQuery.data) {
                    true -> icNickBusy
                    false -> icNickOk
                    null -> null
                }
                else -> null
            }
        ) { setEvent(SignUpContract.Event.OnNickInput(it)) }
        Input(
            hint = "Email",
            text = state.email,
            icon = icEmail,
            enabled = !isIOInProgress(),
            modifier = Modifier.padding(top = elementsPadding),
        ) { setEvent(SignUpContract.Event.OnEmailInput(it)) }
        Input(
            hint = "Пароль",
            text = state.password,
            icon = icPassword,
            type = InputType.PASSWORD,
            enabled = !isIOInProgress(),
            modifier = Modifier.padding(top = elementsPadding),
        ) { setEvent(SignUpContract.Event.OnPasswordInput(it)) }
        Button(
            text = "Продолжить",
            isLoading = state.signUpProgress,
            modifier = Modifier.fillMaxWidth().padding(top = elementsPadding)
        ) { setEvent(SignUpContract.Event.OnSignUpClicked) }
        SocialButton(
            "Создать через Вконтакте",
            icVk,
            isEnabled = !isIOInProgress(),
            isLoading = state.vkSignUpProgress,
            modifier = Modifier.padding(top = elementsPadding),
        ) { setEvent(SignUpContract.Event.OnVkSignUpClicked) }
        SocialButton(
            "Создать через Google",
            icGoogle,
            isEnabled = !isIOInProgress(),
            isLoading = state.googleSignUpProgress,
            modifier = Modifier.padding(top = elementsPadding/2),
        ) { setEvent(SignUpContract.Event.OnGoogleSignUpClicked) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = elementsPadding),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Назад",
                color = AppTheme.colors.labelSecondary,
                fontSize = 12.sp,
                modifier = Modifier.clickable(onClick = navigateBack)
            )
        }
    }
}
//
//@Preview
//@Composable
//fun SignUpPreview() {
//    SignUp(SignUpContract.State(), {}, {})
//}