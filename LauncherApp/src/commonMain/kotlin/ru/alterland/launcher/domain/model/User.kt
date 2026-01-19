package ru.alterland.launcher.domain.model

data class User(
    val id: String,
    val email: String,
    val nickname: String,
    val realName: String,
    val role: Role?,
    val skin: Skin?
)
