package ru.alterland.launcher.domain.model

data class User(
    val id: String,
    val email: String,
    val nickname: String,
    val realName: String,
    val role: Role?
) {
    data class Role(
        val id: String,
        val name: String,
        val strength: Int
    ) {
        companion object {
            const val DEFAULT_STRENGTH = 0
            const val MIN_EDIT_STRENGTH = 3
        }
    }
}
