package ru.alterland.launcher.util.base

sealed class Resource<T> {
    class Idle<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    data class Content<T>(val data : T) : Resource<T>()
    data class Error<T>(val data: T? = null, val throwable : Throwable) : Resource<T>()

    fun getOrNull(): T? = (this as? Content<T>)?.data ?: (this as? Error<T>)?.data
}
