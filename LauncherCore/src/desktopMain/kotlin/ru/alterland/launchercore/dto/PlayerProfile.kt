package ru.alterland.launchercore.dto

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class PlayerProfile(
    val uuid: Uuid,
    val username: String,
    val properties: Map<String, String>
)
