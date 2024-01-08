package ru.alterland.launcher.data.source.local

import kotlinx.serialization.Serializable

@Serializable
internal data class Persistent(
    val settings: MutableMap<String, String>,
    val cookies: MutableMap<String, List<PersistentCookie>> = mutableMapOf()
)
