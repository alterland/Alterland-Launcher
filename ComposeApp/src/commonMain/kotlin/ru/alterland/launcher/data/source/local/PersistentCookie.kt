package ru.alterland.launcher.data.source.local

import kotlinx.serialization.Serializable

@Serializable
data class PersistentCookie(
    val name: String,
    val value: String,
    val maxAge: Int? = null,
    val expires: Long? = null,
    val domain: String? = null,
    val path: String? = "/",
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
    val extensions: Map<String, String?>? = null
)
