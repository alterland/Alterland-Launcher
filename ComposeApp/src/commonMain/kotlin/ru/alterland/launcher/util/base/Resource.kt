package base

import ru.alterland.launcher.util.base.AppException

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Content<T>(val data : T) : Resource<T>()
    data class Error<T>(val exception : AppException) : Resource<T>()
}
