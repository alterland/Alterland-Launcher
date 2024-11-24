package ru.alterland.launcher.ui.screen.auth.sign_up

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
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.Input
import ru.alterland.launcher.ui.widgets.InputType
import ru.alterland.launcher.util.base.Resource

@Composable
fun SignUp(
    state: SignUpContract.State,
    onEvent: (e: SignUpContract.Event) -> Unit,
    navigateBack: () -> Unit
) {

    fun isIOInProgress() = state.signUpProgress || state.vkSignUpProgress || state.googleSignUpProgress

    val elementsPadding = 22.dp

    val icNickOk = painterResource(Res.drawable.ic_check)
    val icNickBusy = painterResource(Res.drawable.ic_dont)
    val icEmail = painterResource(Res.drawable.ic_email)
    val icPassword = painterResource(Res.drawable.ic_password)
    val icVk = painterResource(Res.drawable.ic_vk)
    val icGoogle = painterResource(Res.drawable.ic_google)

    Column {
        Input(
            hint = stringResource(Res.string.nickname),
            text = state.nickName,
            icon = painterResource(Res.drawable.ic_login),
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
        ) { onEvent(SignUpContract.Event.OnNickInput(it)) }
        Input(
            hint = stringResource(Res.string.email),
            text = state.email,
            icon = icEmail,
            enabled = !isIOInProgress(),
            modifier = Modifier.padding(top = elementsPadding),
        ) { onEvent(SignUpContract.Event.OnEmailInput(it)) }
        Input(
            hint = stringResource(Res.string.password),
            text = state.password,
            icon = icPassword,
            type = InputType.PASSWORD,
            enabled = !isIOInProgress(),
            modifier = Modifier.padding(top = elementsPadding),
        ) { onEvent(SignUpContract.Event.OnPasswordInput(it)) }
        Button(
            text = stringResource(Res.string.string_continue),
            isLoading = state.signUpProgress,
            modifier = Modifier.fillMaxWidth().padding(top = elementsPadding)
        ) { onEvent(SignUpContract.Event.OnSignUpClicked) }
//        SocialButton(
//            text = stringResource(Res.string.vk_sign_up),
//            icVk,
//            isEnabled = !isIOInProgress(),
//            isLoading = state.vkSignUpProgress,
//            modifier = Modifier.padding(top = elementsPadding),
//        ) { onEvent(SignUpContract.Event.OnVkSignUpClicked) }
//        SocialButton(
//            text = stringResource(Res.string.google_sign_up),
//            icGoogle,
//            isEnabled = !isIOInProgress(),
//            isLoading = state.googleSignUpProgress,
//            modifier = Modifier.padding(top = elementsPadding/2),
//        ) { onEvent(SignUpContract.Event.OnGoogleSignUpClicked) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = elementsPadding),
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

@Preview
@Composable
fun SignUpPreview() {
    SignUp(SignUpContract.State(), {}, {})
}
