package ru.alterland.launcher.domain.entity

data class AppError(
    val id: String,
    val error: Throwable
)
