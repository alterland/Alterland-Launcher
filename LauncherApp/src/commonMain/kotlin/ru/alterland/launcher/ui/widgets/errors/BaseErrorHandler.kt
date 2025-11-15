package ru.alterland.launcher.ui.widgets.errors

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.alterland.launcher.ui.model.AppErrorUi

@Composable
fun BaseErrorHandler(
    modifier: Modifier = Modifier,
    itemsModifier: Modifier = Modifier,
    errors: List<AppErrorUi> = listOf(),
    reverseLayout: Boolean = false,
    onErrorClose: (String) -> Unit
) {
    if (errors.isEmpty()) return
//    val messages = errors.map { appError ->
//        val message = when (val exception = appError.error) {
//            is AppException.ValidationException -> exception.fields.entries.joinToString {
//                it.value.joinToString { validationException ->
//                    when (validationException) {
//                        is AppException.ValidationException.TooLongException -> {
//                            "Поле ${it.key} должно быть короче ${validationException.max + 1} символов"
//                        }
//
//                        is AppException.ValidationException.TooShortException -> {
//                            "Поле ${it.key} должно быть длиннее ${validationException.min - 1} символов"
//                        }
//
//                        AppException.ValidationException.InvalidException -> "${it.key}: неверный формат поля"
//                        AppException.ValidationException.EmptyFieldException -> "${it.key}: заполните поле"
//                        else -> "Ошибка валидации запроса"
//                    }
//                }
//            }
//            is AppException.UpdateException -> pluralStringResource(Res.plurals.error_update_files, exception.errorCount, exception.errorCount)
//            is AppException -> {
//                when (exception) {
//                    AppException.AlreadySignedInException -> "Вы уже выполнили вход"
//                    AppException.CantAccessEmailException -> "Не удалось получить почту из вашего профиля в соц. сети"
//                    AppException.ClientException -> "Ошибка"
//                    AppException.EmailExistsException -> "Пользователь с таким email уже зарегистрирован"
//                    AppException.InvalidCodeException -> "Неверный код"
//                    AppException.InvalidCredentialsException -> "Неверные данные для входа"
//                    AppException.LogOutFirstException -> "Сначала выйдите из аккаунта"
//                    AppException.NicknameExistsException -> "Пользователь с таким ником уже зарегистрирован"
//                    AppException.NoInternetException -> "Отсутствует соединение"
//                    AppException.ServerException -> "Ошибка на стороне сервера. Попробуйте позже."
//                    AppException.SignUpUnknownException -> "Неизвестная ошибка во время регистрации"
//                    AppException.TokenNotFoundException -> "Токен не найден"
//                    AppException.AccessTokenRequiredException -> "Не передан токен"
//                    AppException.UnsupportedSocialException -> "Вход через данную соц. сеть не поддерживается"
//                    AppException.UserNotFoundException -> "Пользователь не найден"
//                    else -> "Ошибка"
//                }
//            }
//            else -> exception.message ?: "Неизвестная ошибка"
//        }
//        ErrorMessage(
//            id = appError.id,
//            message = message
//        )
//    }

    LazyColumn(
        modifier = modifier,
        reverseLayout = reverseLayout
    ) {
        items(errors) { error ->
            ErrorHolder(
                modifier = itemsModifier,
                appError = error,
                onErrorClose = onErrorClose
            )
        }
    }
}
