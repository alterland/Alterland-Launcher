package ru.alterland.launcher.domain.model

data class AppError(
    val id: String,
    val error: Throwable
)
