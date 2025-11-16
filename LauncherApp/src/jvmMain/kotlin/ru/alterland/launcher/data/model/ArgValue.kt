package ru.alterland.launcher.data.model

enum class ArgValue(val value: String) {

    AUTH_PLAYER_NAME($$"${auth_player_name}"),
    VERSION_NAME($$"${version_name}"),
    GAME_DIRECTORY($$"${game_directory}"),
    AUTH_UUID($$"${auth_uuid}"),
    AUTH_ACCESS_TOKEN("\${auth_access_token}"),
    USER_TYPE($$"${user_type}"),
    VERSION_TYPE($$"${version_type}"),
    LAUNCHER_NAME($$"${launcher_name}"),
    LAUNCHER_VERSION($$"${launcher_version}"),
    CLASSPATH($$"${classpath}"),
    WORK_DIR($$"${work_dir}"),
    CLASSPATH_SEPARATOR($$"${classpath_separator}"),
    UNKNOWN("");

    companion object {
        fun fromValue(value: String?): ArgValue = entries.firstOrNull {
            it.value.startsWith(value?.lowercase() ?: "")
        } ?: UNKNOWN
    }
}
