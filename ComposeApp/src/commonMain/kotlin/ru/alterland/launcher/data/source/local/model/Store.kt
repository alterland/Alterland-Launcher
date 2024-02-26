package ru.alterland.launcher.data.source.local.model

import kotlinx.serialization.Serializable
import ru.alterland.launcher.data.source.local.PersistentCookie

@Serializable
data class Store(
    val settings: Map<String, String>,
    val cookies: Map<String, List<PersistentCookie>> = mapOf()
)
