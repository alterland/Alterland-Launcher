package ru.alterland.launcher.util.base

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Content<T>(val data : T) : Resource<T>()
    data class Error<T>(val throwable : Throwable) : Resource<T>()

    fun getOrNull(): T? = (this as? Content<T>)?.data
}
