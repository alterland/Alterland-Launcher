package ru.alterland.launcher.ui.screen.main.editserver

import kotlinx.serialization.Serializable

@Serializable
data class EditServerPayload(
    val mode: EditServerMode
)
