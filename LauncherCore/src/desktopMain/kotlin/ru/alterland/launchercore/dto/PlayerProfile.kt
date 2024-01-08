package ru.alterland.launchercore.dto

import java.util.*

data class PlayerProfile(
    val uuid: UUID,
    val username: String,
//    val assets: Map<String, Texture>,
    val properties: Map<String, String>
)
