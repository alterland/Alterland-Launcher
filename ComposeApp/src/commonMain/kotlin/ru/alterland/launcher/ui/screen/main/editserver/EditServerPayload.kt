package ru.alterland.launcher.ui.screen.main.editserver

sealed class EditServerPayload {
    data object Add: EditServerPayload()
    data class Edit(val serverProfileId: String): EditServerPayload()
}
