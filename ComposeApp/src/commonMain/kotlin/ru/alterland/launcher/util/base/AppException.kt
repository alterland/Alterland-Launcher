package ru.alterland.launcher.util.base

sealed class AppException: Exception() {
    data object UserNotFoundException: AppException()
    data object EmailExistsException: AppException()
    data object AlreadySignedInException: AppException()
    data object InvalidCodeException: AppException()
    data object InvalidCredentialsException: AppException()
    data object NicknameExistsException: AppException()

    class ValidationException(val fields: Map<String, List<AppException>>): AppException() {
        data object EmptyFieldException: AppException()
        data object InvalidException: AppException()
        data class TooLongException(val max: Int): AppException()
        data class TooShortException(val min: Int): AppException()
    }

    data object AccessTokenRequiredException: AppException()
    data object TokenNotFoundException: AppException()
    data object LogOutFirstException: AppException()
    data object SignUpUnknownException: AppException()

    data object UnsupportedSocialException: AppException()
    data object CantAccessEmailException: AppException()

    data object ServerException: AppException()
    data object ClientException: AppException()
    data object NoInternetException: AppException()

    companion object {
        fun createAppException(message: String) = when(message) {
            "user_not_found" -> UserNotFoundException
            "access_token_required" -> AccessTokenRequiredException
            "token_not_found" -> TokenNotFoundException
            "logout_first" -> LogOutFirstException
            "unsupported_social" -> UnsupportedSocialException
            "cant_access_email" -> CantAccessEmailException
            "email_already_exists" -> EmailExistsException
            "already_signed_in" -> AlreadySignedInException
            "nickname_already_exists" -> NicknameExistsException
            "signup_unknown_exeption" -> SignUpUnknownException
            "invalid_code" -> InvalidCodeException
            "invalid_credentials" -> InvalidCredentialsException
            else -> ClientException
        }

        fun createAppException(fieldErrors: List<String>): AppException {
            val fields = mutableMapOf<String, MutableList<AppException>>()
            fieldErrors.map { fieldError ->
                val e = fieldError.split(ERROR_DELIMITER)
                if (e.size > 1) {
                    val key = e[0]
                    val value = e[1]
                    val error = when {
                        value.contains(MIN_ERROR) -> {
                            val min = value.substring(MIN_ERROR.length, value.length).toInt()
                            ValidationException.TooShortException(min = min)
                        }
                        value.contains(MAX_ERROR) -> {
                            val max = value.substring(MAX_ERROR.length, value.length).toInt()
                            ValidationException.TooLongException(max = max)
                        }
                        value == REQUIRED_ERROR -> ValidationException.EmptyFieldException
                        else -> ValidationException.InvalidException
                    }
                    val prevEntry = fields[key]
                    if (prevEntry == null) {
                        fields[key] = mutableListOf(error)
                    } else {
                        fields[key] = prevEntry.apply { add(error) }
                    }
                }
            }
            return ValidationException(fields = fields)
        }

        private const val ERROR_DELIMITER = "_field_"
        private const val REQUIRED_ERROR = "required"
        private const val INVALID_ERROR = "invalid"
        private const val MIN_ERROR = "min_"
        private const val MAX_ERROR = "max_"
    }
}
