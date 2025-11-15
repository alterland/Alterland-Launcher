package ru.alterland.launcher.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class AppErrorUi(
    val id: String,
    val message: String
)
